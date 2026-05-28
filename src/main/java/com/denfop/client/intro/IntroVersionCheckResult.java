package com.denfop.client.intro;

public record IntroVersionCheckResult(
        String mcVersion,
        String localVersion,
        String remoteVersion,
        boolean remoteAvailable,
        boolean updateRecommended,
        String checkedUrl
) {
}
