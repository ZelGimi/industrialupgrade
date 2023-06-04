package com.denfop.api.gui;

import com.google.common.base.Supplier;
import ic2.core.init.Localization;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TextProvider {

    public TextProvider() {
    }

    public static TextProvider.ITextProvider of(String text) {
        return text.isEmpty() ? new ConstantEmpty() : new Constant(text);
    }

    public static TextProvider.ITextProvider of(final Supplier<String> supplier) {
        return new TextProvider.AbstractTextProvider() {
            public String getRaw(Object base, Map<String, TextProvider.ITextProvider> tokens) {
                return supplier.get();
            }

            public String getConstant(Class<?> baseClass) {
                return supplier.get();
            }
        };
    }

    public static TextProvider.ITextProvider ofTranslated(String key) {
        return new TextProvider.Translate(new TextProvider.Constant(key));
    }

    public static TextProvider.ITextProvider parse(String text, Class<?> baseClass) {
        Queue<List<TextProvider.AbstractTextProvider>> continuations = Collections.asLifoQueue(new ArrayDeque());
        StringBuilder continuationTypes = new StringBuilder();
        char currentType = 0;
        List<TextProvider.AbstractTextProvider> providers = new ArrayList();
        StringBuilder part = new StringBuilder(text.length());
        boolean escaped = false;

        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (escaped) {
                part.append(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '{') {
                finish(part, providers);
                continuations.add(providers);
                continuationTypes.append(currentType);
                currentType = c;
                providers = new ArrayList();
            } else if (currentType == '{' && c == ',') {
                finish(part, providers);
                providers.add(null);
            } else {
                TextProvider.AbstractTextProvider format;
                if (currentType == '{' && c == '}') {
                    finish(part, providers);
                    format = null;
                    List<TextProvider.AbstractTextProvider> args = new ArrayList();
                    int start = 0;

                    for (int j = start; j < providers.size(); ++j) {
                        if (((List) providers).get(j) == null) {
                            TextProvider.AbstractTextProvider provider = getProvider(providers, start, j);
                            if (format == null) {
                                format = provider;
                            } else {
                                args.add(provider);
                            }

                            start = j + 1;
                        }
                    }

                    TextProvider.AbstractTextProvider provider = getProvider(providers, start, providers.size());
                    if (format == null) {
                        format = provider;
                    } else {
                        args.add(provider);
                    }

                    if (args.isEmpty()) {
                        provider = new TextProvider.Translate(format);
                    } else {
                        provider = new TextProvider.TranslateFormat(format, args);
                    }

                    providers = continuations.remove();
                    currentType = continuationTypes.charAt(continuationTypes.length() - 1);
                    continuationTypes.setLength(continuationTypes.length() - 1);
                    providers.add(provider);
                } else if (c == '%') {
                    if (currentType != '%') {
                        if (i + 1 < text.length() && text.charAt(i + 1) == '%') {
                            part.append('%');
                            ++i;
                        } else {
                            finish(part, providers);
                            continuations.add(providers);
                            continuationTypes.append(currentType);
                            currentType = c;
                            providers = new ArrayList();
                        }
                    } else {
                        finish(part, providers);
                        format = getResolver(getProvider(providers, 0, providers.size()), baseClass);
                        providers = continuations.remove();
                        currentType = continuationTypes.charAt(continuationTypes.length() - 1);
                        continuationTypes.setLength(continuationTypes.length() - 1);
                        providers.add(format);
                    }
                } else {
                    part.append(c);
                }
            }
        }

        finish(part, providers);
        if (currentType != 0) {
            return new TextProvider.Constant("ERROR: unfinished token " + currentType + " in " + text);
        } else if (escaped) {
            return new TextProvider.Constant("ERROR: unfinished escape sequence in " + text);
        } else {
            return getProvider(providers, 0, providers.size());
        }
    }

    private static void finish(StringBuilder part, List<TextProvider.AbstractTextProvider> providers) {
        if (part.length() != 0) {
            providers.add(new TextProvider.Constant(part.toString()));
            part.setLength(0);
        }
    }

    private static TextProvider.AbstractTextProvider getProvider(
            List<TextProvider.AbstractTextProvider> providers,
            int start,
            int end
    ) {
        assert start <= end;

        if (start == end) {
            return new TextProvider.ConstantEmpty();
        } else {
            return start + 1 == end
                    ? providers.get(start)
                    : new Merge(new ArrayList(providers.subList(start, end)));
        }
    }

    private static TextProvider.AbstractTextProvider getResolver(TextProvider.AbstractTextProvider token, Class<?> baseClass) {
        String staticToken = token.getConstant(baseClass);
        if (staticToken == null) {
            return new TextProvider.TokenResolverDynamic(token);
        } else {
            String staticResult = resolveToken(staticToken, baseClass, null, emptyTokens());
            return staticResult != null
                    ? new Constant(staticResult)
                    : new TokenResolverStatic(staticToken);
        }
    }

    private static String resolveToken(
            String token,
            Class<?> baseClass,
            Object base,
            Map<String, TextProvider.ITextProvider> tokens
    ) {
        TextProvider.ITextProvider ret = tokens.get(token);
        if (ret != null) {
            return ret instanceof TextProvider.AbstractTextProvider ? ((TextProvider.AbstractTextProvider) ret).getRaw(
                    base,
                    tokens
            ) : ret.get(base, tokens);
        } else if (baseClass == null) {
            return null;
        } else if (token.startsWith("base.")) {
            Object value = retrieve(token, "base.".length(), baseClass, base);
            return toString(value);
        } else {
            return null;
        }
    }

    private static Object retrieve(String path, int start, Class<?> subjectClass, Object subject) {
        int end;
        do {
            end = path.indexOf(46, start);
            if (end == -1) {
                end = path.length();
            }

            String part = path.substring(start, end);
            if (part.endsWith("()")) {
                part = part.substring(0, part.length() - "()".length());
                Method method = getMethodOptional(subjectClass, part);
                if (method == null) {
                    return null;
                }

                subject = invokeMethodOptional(method, subject);
                if (subject == null) {
                    return null;
                }

                subjectClass = subject.getClass();
            } else {
                Field field = getFieldOptional(subjectClass, part);
                if (field == null) {
                    return null;
                }

                subject = getFieldValueOptional(field, subject);
                if (subject == null) {
                    return null;
                }

                subjectClass = subject.getClass();
            }

            start = end + 1;
        } while (end != path.length());

        return subject;
    }

    private static Method getMethodOptional(Class<?> cls, String name) {
        try {
            return cls.getMethod(name);
        } catch (NoSuchMethodException var3) {
            return null;
        } catch (SecurityException var4) {
            throw new RuntimeException(var4);
        }
    }

    private static Object invokeMethodOptional(Method method, Object obj) {
        if (obj == null && !Modifier.isStatic(method.getModifiers())) {
            return null;
        } else {
            Object ret;
            try {
                ret = method.invoke(obj);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }

            if (ret == null) {
            }

            return ret;
        }
    }

    private static Field getFieldOptional(Class<?> cls, String name) {
        try {
            return cls.getField(name);
        } catch (NoSuchFieldException var3) {
            return null;
        } catch (SecurityException var4) {
            throw new RuntimeException(var4);
        }
    }

    private static Object getFieldValueOptional(Field field, Object obj) {
        if (obj == null && !Modifier.isStatic(field.getModifiers())) {
            return null;
        } else {
            Object ret;
            try {
                ret = field.get(obj);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }

            if (ret == null) {
            }

            return ret;
        }
    }

    private static String toString(Object o) {
        return o == null ? null : o.toString();
    }

    public static Map<String, TextProvider.ITextProvider> emptyTokens() {
        return Collections.emptyMap();
    }

    public interface ITextProvider {

        String get(Object var1, Map<String, TextProvider.ITextProvider> var2);

        String getOptional(Object var1, Map<String, TextProvider.ITextProvider> var2);

    }

    private static class TokenResolverStatic extends TextProvider.AbstractTextProvider {

        private final String token;

        public TokenResolverStatic(String token) {
            super();
            this.token = token;
        }

        public String getRaw(Object base, Map<String, TextProvider.ITextProvider> tokens) {
            return TextProvider.resolveToken(this.token, base != null ? base.getClass() : null, base, tokens);
        }

        public String getConstant(Class<?> baseClass) {
            return TextProvider.resolveToken(this.token, baseClass, null, TextProvider.emptyTokens());
        }

    }

    private static class TokenResolverDynamic extends TextProvider.AbstractTextProvider {

        private final TextProvider.AbstractTextProvider token;

        public TokenResolverDynamic(TextProvider.AbstractTextProvider token) {
            super();
            this.token = token;
        }

        public String getRaw(Object base, Map<String, TextProvider.ITextProvider> tokens) {
            String token = this.token.getRaw(base, tokens);
            return token == null ? null : TextProvider.resolveToken(token, base != null ? base.getClass() : null, base, tokens);
        }

        public String getConstant(Class<?> baseClass) {
            String token = this.token.getConstant(baseClass);
            return token == null ? null : TextProvider.resolveToken(token, baseClass, null, TextProvider.emptyTokens());
        }

    }

    private static class TranslateFormat extends TextProvider.AbstractTextProvider {

        private final TextProvider.AbstractTextProvider format;
        private final List<TextProvider.AbstractTextProvider> args;

        public TranslateFormat(TextProvider.AbstractTextProvider format, List<TextProvider.AbstractTextProvider> args) {
            super();
            this.format = format;
            this.args = args;
        }

        public String getRaw(Object base, Map<String, TextProvider.ITextProvider> tokens) {
            String format = this.format.getRaw(base, tokens);
            if (format == null) {
                return null;
            } else {
                Object[] cArgs = new Object[this.args.size()];

                for (int i = 0; i < this.args.size(); ++i) {
                    String arg = this.args.get(i).getRaw(base, tokens);
                    if (arg == null) {
                        return null;
                    }

                    cArgs[i] = arg;
                }

                return Localization.translate(format, cArgs);
            }
        }

        public String getConstant(Class<?> baseClass) {
            return null;
        }

    }

    private static class Translate extends TextProvider.AbstractTextProvider {

        private final TextProvider.AbstractTextProvider key;

        public Translate(TextProvider.AbstractTextProvider key) {
            super();
            this.key = key;
        }

        public String getRaw(Object base, Map<String, TextProvider.ITextProvider> tokens) {
            String key = this.key.getRaw(base, tokens);
            return key == null ? null : Localization.translate(key);
        }

        public String getConstant(Class<?> baseClass) {
            return null;
        }

    }

    private static class Merge extends TextProvider.AbstractTextProvider {

        private final List<TextProvider.AbstractTextProvider> providers;

        public Merge(List<TextProvider.AbstractTextProvider> providers) {
            super();
            this.providers = providers;
        }

        public String getRaw(Object base, Map<String, TextProvider.ITextProvider> tokens) {
            StringBuilder ret = new StringBuilder();
            Iterator var4 = this.providers.iterator();

            while (var4.hasNext()) {
                TextProvider.AbstractTextProvider provider = (TextProvider.AbstractTextProvider) var4.next();
                String part = provider.getRaw(base, tokens);
                if (part == null) {
                    return null;
                }

                ret.append(part);
            }

            return ret.toString();
        }

        public String getConstant(Class<?> baseClass) {
            StringBuilder ret = new StringBuilder();
            Iterator var3 = this.providers.iterator();

            while (var3.hasNext()) {
                TextProvider.AbstractTextProvider provider = (TextProvider.AbstractTextProvider) var3.next();
                String part = provider.getConstant(baseClass);
                if (part == null) {
                    return null;
                }

                ret.append(part);
            }

            return ret.toString();
        }

    }

    private static class ConstantEmpty extends TextProvider.AbstractTextProvider {

        private ConstantEmpty() {
            super();
        }

        public String getRaw(Object base, Map<String, TextProvider.ITextProvider> tokens) {
            return "";
        }

        public String getConstant(Class<?> baseClass) {
            return "";
        }

    }

    private static class Constant extends TextProvider.AbstractTextProvider {

        private final String text;

        public Constant(String text) {
            super();
            this.text = text;
        }

        public String getRaw(Object base, Map<String, TextProvider.ITextProvider> tokens) {
            return this.text;
        }

        public String getConstant(Class<?> baseClass) {
            return this.text;
        }

    }

    private abstract static class AbstractTextProvider implements TextProvider.ITextProvider {

        private AbstractTextProvider() {
        }

        public final String get(Object base, Map<String, TextProvider.ITextProvider> tokens) {
            String result = this.getRaw(base, tokens);
            return result != null ? result : "ERROR";
        }

        public final String getOptional(Object base, Map<String, TextProvider.ITextProvider> tokens) {
            return this.getRaw(base, tokens);
        }

        protected abstract String getRaw(Object var1, Map<String, TextProvider.ITextProvider> var2);

        protected abstract String getConstant(Class<?> var1);

    }

}
