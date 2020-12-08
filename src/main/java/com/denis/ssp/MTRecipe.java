package com.Denfop.ssp;

final class MTRecipe {
  public final int lineNumber;
  
  public final String[] parts;
  
  public MTRecipe(int lineNumber, String line) {
    this.lineNumber = lineNumber;
    this.parts = line.split(";\\s*");
  }
  
  public boolean isValid() {
    return (this.parts.length == 3);
  }
}
