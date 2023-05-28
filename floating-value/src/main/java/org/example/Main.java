package org.example;

public class Main {
  static void doubleValue(double value){
    long bits = Double.doubleToLongBits(value);

    boolean negative = (bits & 0x8000000000000000L) != 0;     //  1 bit
    long exponent =     bits & 0x7ff0000000000000L;           // 11 bit
    long mantissa =     bits & 0x000fffffffffffffL;           // 52 bit

    System.out.println("negative: " + negative);
    System.out.println("exponent: " + Double.longBitsToDouble(exponent));
    System.out.println("mantissa: " + Double.longBitsToDouble(mantissa));

    System.out.println( Double.longBitsToDouble(mantissa) * Math.pow(2, Double.longBitsToDouble(exponent)) );
    System.out.println(value / 256);
    System.out.println(0.05859375 * 256);
  }

  static void floatValue(float value){
    int bits = Float.floatToIntBits(value);

    boolean negative = (bits & 0x80000000) != 0;     //  1 bit
    int exponent =      bits & 0x7f800000;           //  8 bit
    int mantissa =      bits & 0x007fffff;           // 23 bit

    System.out.println("negative: " + negative);
    System.out.println("exponent: " + exponent);
    System.out.println("mantissa: " + mantissa);

  }

  public static void main(String[] args) {
    //doubleValue(15.00);
    floatValue(5.3f);
    // https://stackoverflow.com/questions/24273994/how-do-you-get-the-mantissa-of-a-float-in-java

    // 5.3 (base 10)
    // 101.0100110011001100110011 (base 2)
    int value = 0b0_10000001_01010011001100110011001;
    //              10000001_01010011001100110011010
    System.out.println(Float.intBitsToFloat(value));

    int bits = Float.floatToIntBits(5.3f);
    System.out.println(Integer.toBinaryString(bits));
  }
}