package CustomOreGen.Util;

import CustomOreGen.Util.NoiseGenerator$Grad;
import java.util.Random;

public class NoiseGenerator
{
    private static NoiseGenerator$Grad[] grad2 = new NoiseGenerator$Grad[] {new NoiseGenerator$Grad(1.0D, 0.0D, 0.0D), new NoiseGenerator$Grad(-1.0D, 0.0D, 0.0D), new NoiseGenerator$Grad(0.0D, 1.0D, 0.0D), new NoiseGenerator$Grad(0.0D, -1.0D, 0.0D)};
    private static NoiseGenerator$Grad[] grad3 = new NoiseGenerator$Grad[] {new NoiseGenerator$Grad(1.0D, 1.0D, 0.0D), new NoiseGenerator$Grad(-1.0D, 1.0D, 0.0D), new NoiseGenerator$Grad(1.0D, -1.0D, 0.0D), new NoiseGenerator$Grad(-1.0D, -1.0D, 0.0D), new NoiseGenerator$Grad(1.0D, 0.0D, 1.0D), new NoiseGenerator$Grad(-1.0D, 0.0D, 1.0D), new NoiseGenerator$Grad(1.0D, 0.0D, -1.0D), new NoiseGenerator$Grad(-1.0D, 0.0D, -1.0D), new NoiseGenerator$Grad(0.0D, 1.0D, 1.0D), new NoiseGenerator$Grad(0.0D, -1.0D, 1.0D), new NoiseGenerator$Grad(0.0D, 1.0D, -1.0D), new NoiseGenerator$Grad(0.0D, -1.0D, -1.0D)};
    private static NoiseGenerator$Grad[] grad4 = new NoiseGenerator$Grad[] {new NoiseGenerator$Grad(0.0D, 1.0D, 1.0D, 1.0D), new NoiseGenerator$Grad(0.0D, 1.0D, 1.0D, -1.0D), new NoiseGenerator$Grad(0.0D, 1.0D, -1.0D, 1.0D), new NoiseGenerator$Grad(0.0D, 1.0D, -1.0D, -1.0D), new NoiseGenerator$Grad(0.0D, -1.0D, 1.0D, 1.0D), new NoiseGenerator$Grad(0.0D, -1.0D, 1.0D, -1.0D), new NoiseGenerator$Grad(0.0D, -1.0D, -1.0D, 1.0D), new NoiseGenerator$Grad(0.0D, -1.0D, -1.0D, -1.0D), new NoiseGenerator$Grad(1.0D, 0.0D, 1.0D, 1.0D), new NoiseGenerator$Grad(1.0D, 0.0D, 1.0D, -1.0D), new NoiseGenerator$Grad(1.0D, 0.0D, -1.0D, 1.0D), new NoiseGenerator$Grad(1.0D, 0.0D, -1.0D, -1.0D), new NoiseGenerator$Grad(-1.0D, 0.0D, 1.0D, 1.0D), new NoiseGenerator$Grad(-1.0D, 0.0D, 1.0D, -1.0D), new NoiseGenerator$Grad(-1.0D, 0.0D, -1.0D, 1.0D), new NoiseGenerator$Grad(-1.0D, 0.0D, -1.0D, -1.0D), new NoiseGenerator$Grad(1.0D, 1.0D, 0.0D, 1.0D), new NoiseGenerator$Grad(1.0D, 1.0D, 0.0D, -1.0D), new NoiseGenerator$Grad(1.0D, -1.0D, 0.0D, 1.0D), new NoiseGenerator$Grad(1.0D, -1.0D, 0.0D, -1.0D), new NoiseGenerator$Grad(-1.0D, 1.0D, 0.0D, 1.0D), new NoiseGenerator$Grad(-1.0D, 1.0D, 0.0D, -1.0D), new NoiseGenerator$Grad(-1.0D, -1.0D, 0.0D, 1.0D), new NoiseGenerator$Grad(-1.0D, -1.0D, 0.0D, -1.0D), new NoiseGenerator$Grad(1.0D, 1.0D, 1.0D, 0.0D), new NoiseGenerator$Grad(1.0D, 1.0D, -1.0D, 0.0D), new NoiseGenerator$Grad(1.0D, -1.0D, 1.0D, 0.0D), new NoiseGenerator$Grad(1.0D, -1.0D, -1.0D, 0.0D), new NoiseGenerator$Grad(-1.0D, 1.0D, 1.0D, 0.0D), new NoiseGenerator$Grad(-1.0D, 1.0D, -1.0D, 0.0D), new NoiseGenerator$Grad(-1.0D, -1.0D, 1.0D, 0.0D), new NoiseGenerator$Grad(-1.0D, -1.0D, -1.0D, 0.0D)};
    private final byte[] permutation = new byte[] {(byte)17, (byte)1, (byte)31, (byte)92, (byte)22, (byte)53, (byte)127, (byte)38, (byte)84, (byte)62, (byte)54, (byte)21, (byte)123, (byte)111, (byte)49, (byte)96, (byte)95, (byte)58, (byte)104, (byte)42, (byte)34, (byte)55, (byte)78, (byte)107, (byte)105, (byte)98, (byte)39, (byte)50, (byte)125, (byte)2, (byte)91, (byte)23, (byte)119, (byte)100, (byte)70, (byte)56, (byte)3, (byte)88, (byte)66, (byte)101, (byte)29, (byte)8, (byte)43, (byte)76, (byte)124, (byte)0, (byte)15, (byte)115, (byte)83, (byte)5, (byte)19, (byte)64, (byte)106, (byte)80, (byte)48, (byte)82, (byte)121, (byte)69, (byte)117, (byte)10, (byte)61, (byte)9, (byte)109, (byte)87, (byte)94, (byte)25, (byte)20, (byte)27, (byte)102, (byte)122, (byte)60, (byte)6, (byte)33, (byte)77, (byte)89, (byte)90, (byte)118, (byte)110, (byte)75, (byte)71, (byte)113, (byte)126, (byte)67, (byte)52, (byte)74, (byte)116, (byte)44, (byte)103, (byte)14, (byte)18, (byte)93, (byte)85, (byte)108, (byte)12, (byte)32, (byte)45, (byte)47, (byte)79, (byte)68, (byte)86, (byte)24, (byte)114, (byte)30, (byte)57, (byte)59, (byte)81, (byte)4, (byte)26, (byte)36, (byte)37, (byte)120, (byte)35, (byte)65, (byte)28, (byte)7, (byte)63, (byte)40, (byte)72, (byte)73, (byte)41, (byte)99, (byte)16, (byte)97, (byte)13, (byte)112, (byte)51, (byte)11, (byte)46};
    private static final double F2 = 0.5D * (Math.sqrt(3.0D) - 1.0D);
    private static final double G2 = (3.0D - Math.sqrt(3.0D)) / 6.0D;
    private static final double F3 = 0.3333333333333333D;
    private static final double G3 = 0.16666666666666666D;
    private static final double F4 = (Math.sqrt(5.0D) - 1.0D) / 4.0D;
    private static final double G4 = (5.0D - Math.sqrt(5.0D)) / 20.0D;

    public NoiseGenerator() {}

    public NoiseGenerator(Random var1)
    {
        if (var1 != null)
        {
            for (int var2 = 0; var2 < this.permutation.length; ++var2)
            {
                int var3 = var1.nextInt(this.permutation.length);
                byte var4 = this.permutation[var3];
                this.permutation[var3] = this.permutation[var2];
                this.permutation[var2] = var4;
            }
        }
    }

    public double noise(double var1, double var3)
    {
        double var11 = (var1 + var3) * F2;
        int var13 = fastfloor(var1 + var11);
        int var14 = fastfloor(var3 + var11);
        double var15 = (double)(var13 + var14) * G2;
        double var17 = (double)var13 - var15;
        double var19 = (double)var14 - var15;
        double var21 = var1 - var17;
        double var23 = var3 - var19;
        byte var25;
        byte var26;

        if (var21 > var23)
        {
            var25 = 1;
            var26 = 0;
        }
        else
        {
            var25 = 0;
            var26 = 1;
        }

        double var27 = var21 - (double)var25 + G2;
        double var29 = var23 - (double)var26 + G2;
        double var31 = var21 - 1.0D + 2.0D * G2;
        double var33 = var23 - 1.0D + 2.0D * G2;
        int var35 = var13 & 255;
        int var36 = var14 & 255;
        NoiseGenerator$Grad var37 = this.getGradient(var35, var36);
        NoiseGenerator$Grad var38 = this.getGradient(var35 + var25, var36 + var26);
        NoiseGenerator$Grad var39 = this.getGradient(var35 + 1, var36 + 1);
        double var40 = 0.5D - var21 * var21 - var23 * var23;
        double var5;

        if (var40 < 0.0D)
        {
            var5 = 0.0D;
        }
        else
        {
            var40 *= var40;
            var5 = var40 * var40 * dot(var37, var21, var23);
        }

        double var42 = 0.5D - var27 * var27 - var29 * var29;
        double var7;

        if (var42 < 0.0D)
        {
            var7 = 0.0D;
        }
        else
        {
            var42 *= var42;
            var7 = var42 * var42 * dot(var38, var27, var29);
        }

        double var44 = 0.5D - var31 * var31 - var33 * var33;
        double var9;

        if (var44 < 0.0D)
        {
            var9 = 0.0D;
        }
        else
        {
            var44 *= var44;
            var9 = var44 * var44 * dot(var39, var31, var33);
        }

        return 70.0D * (var5 + var7 + var9);
    }

    public double noise(double var1, double var3, double var5)
    {
        double var15 = (var1 + var3 + var5) * 0.3333333333333333D;
        int var17 = fastfloor(var1 + var15);
        int var18 = fastfloor(var3 + var15);
        int var19 = fastfloor(var5 + var15);
        double var20 = (double)(var17 + var18 + var19) * 0.16666666666666666D;
        double var22 = (double)var17 - var20;
        double var24 = (double)var18 - var20;
        double var26 = (double)var19 - var20;
        double var28 = var1 - var22;
        double var30 = var3 - var24;
        double var32 = var5 - var26;
        byte var34;
        byte var35;
        byte var38;
        byte var39;
        byte var36;
        byte var37;

        if (var28 >= var30)
        {
            if (var30 >= var32)
            {
                var34 = 1;
                var35 = 0;
                var36 = 0;
                var37 = 1;
                var38 = 1;
                var39 = 0;
            }
            else if (var28 >= var32)
            {
                var34 = 1;
                var35 = 0;
                var36 = 0;
                var37 = 1;
                var38 = 0;
                var39 = 1;
            }
            else
            {
                var34 = 0;
                var35 = 0;
                var36 = 1;
                var37 = 1;
                var38 = 0;
                var39 = 1;
            }
        }
        else if (var30 < var32)
        {
            var34 = 0;
            var35 = 0;
            var36 = 1;
            var37 = 0;
            var38 = 1;
            var39 = 1;
        }
        else if (var28 < var32)
        {
            var34 = 0;
            var35 = 1;
            var36 = 0;
            var37 = 0;
            var38 = 1;
            var39 = 1;
        }
        else
        {
            var34 = 0;
            var35 = 1;
            var36 = 0;
            var37 = 1;
            var38 = 1;
            var39 = 0;
        }

        double var40 = var28 - (double)var34 + 0.16666666666666666D;
        double var42 = var30 - (double)var35 + 0.16666666666666666D;
        double var44 = var32 - (double)var36 + 0.16666666666666666D;
        double var46 = var28 - (double)var37 + 0.3333333333333333D;
        double var48 = var30 - (double)var38 + 0.3333333333333333D;
        double var50 = var32 - (double)var39 + 0.3333333333333333D;
        double var52 = var28 - 1.0D + 0.5D;
        double var54 = var30 - 1.0D + 0.5D;
        double var56 = var32 - 1.0D + 0.5D;
        int var58 = var17 & 255;
        int var59 = var18 & 255;
        int var60 = var19 & 255;
        NoiseGenerator$Grad var61 = this.getGradient(var58, var59, var60);
        NoiseGenerator$Grad var62 = this.getGradient(var58 + var34, var59 + var35, var60 + var36);
        NoiseGenerator$Grad var63 = this.getGradient(var58 + var37, var59 + var38, var60 + var39);
        NoiseGenerator$Grad var64 = this.getGradient(var58 + 1, var59 + 1, var60 + 1);
        double var65 = 0.6D - var28 * var28 - var30 * var30 - var32 * var32;
        double var7;

        if (var65 < 0.0D)
        {
            var7 = 0.0D;
        }
        else
        {
            var65 *= var65;
            var7 = var65 * var65 * dot(var61, var28, var30, var32);
        }

        double var67 = 0.6D - var40 * var40 - var42 * var42 - var44 * var44;
        double var9;

        if (var67 < 0.0D)
        {
            var9 = 0.0D;
        }
        else
        {
            var67 *= var67;
            var9 = var67 * var67 * dot(var62, var40, var42, var44);
        }

        double var69 = 0.6D - var46 * var46 - var48 * var48 - var50 * var50;
        double var11;

        if (var69 < 0.0D)
        {
            var11 = 0.0D;
        }
        else
        {
            var69 *= var69;
            var11 = var69 * var69 * dot(var63, var46, var48, var50);
        }

        double var71 = 0.6D - var52 * var52 - var54 * var54 - var56 * var56;
        double var13;

        if (var71 < 0.0D)
        {
            var13 = 0.0D;
        }
        else
        {
            var71 *= var71;
            var13 = var71 * var71 * dot(var64, var52, var54, var56);
        }

        return 32.0D * (var7 + var9 + var11 + var13);
    }

    public double noise(double var1, double var3, double var5, double var7)
    {
        double var19 = (var1 + var3 + var5 + var7) * F4;
        int var21 = fastfloor(var1 + var19);
        int var22 = fastfloor(var3 + var19);
        int var23 = fastfloor(var5 + var19);
        int var24 = fastfloor(var7 + var19);
        double var25 = (double)(var21 + var22 + var23 + var24) * G4;
        double var27 = (double)var21 - var25;
        double var29 = (double)var22 - var25;
        double var31 = (double)var23 - var25;
        double var33 = (double)var24 - var25;
        double var35 = var1 - var27;
        double var37 = var3 - var29;
        double var39 = var5 - var31;
        double var41 = var7 - var33;
        int var43 = 0;
        int var44 = 0;
        int var45 = 0;
        int var46 = 0;

        if (var35 > var37)
        {
            ++var43;
        }
        else
        {
            ++var44;
        }

        if (var35 > var39)
        {
            ++var43;
        }
        else
        {
            ++var45;
        }

        if (var35 > var41)
        {
            ++var43;
        }
        else
        {
            ++var46;
        }

        if (var37 > var39)
        {
            ++var44;
        }
        else
        {
            ++var45;
        }

        if (var37 > var41)
        {
            ++var44;
        }
        else
        {
            ++var46;
        }

        if (var39 > var41)
        {
            ++var45;
        }
        else
        {
            ++var46;
        }

        int var47 = var43 >= 3 ? 1 : 0;
        int var48 = var44 >= 3 ? 1 : 0;
        int var49 = var45 >= 3 ? 1 : 0;
        int var50 = var46 >= 3 ? 1 : 0;
        int var51 = var43 >= 2 ? 1 : 0;
        int var52 = var44 >= 2 ? 1 : 0;
        int var53 = var45 >= 2 ? 1 : 0;
        int var54 = var46 >= 2 ? 1 : 0;
        int var55 = var43 >= 1 ? 1 : 0;
        int var56 = var44 >= 1 ? 1 : 0;
        int var57 = var45 >= 1 ? 1 : 0;
        int var58 = var46 >= 1 ? 1 : 0;
        double var59 = var35 - (double)var47 + G4;
        double var61 = var37 - (double)var48 + G4;
        double var63 = var39 - (double)var49 + G4;
        double var65 = var41 - (double)var50 + G4;
        double var67 = var35 - (double)var51 + 2.0D * G4;
        double var69 = var37 - (double)var52 + 2.0D * G4;
        double var71 = var39 - (double)var53 + 2.0D * G4;
        double var73 = var41 - (double)var54 + 2.0D * G4;
        double var75 = var35 - (double)var55 + 3.0D * G4;
        double var77 = var37 - (double)var56 + 3.0D * G4;
        double var79 = var39 - (double)var57 + 3.0D * G4;
        double var81 = var41 - (double)var58 + 3.0D * G4;
        double var83 = var35 - 1.0D + 4.0D * G4;
        double var85 = var37 - 1.0D + 4.0D * G4;
        double var87 = var39 - 1.0D + 4.0D * G4;
        double var89 = var41 - 1.0D + 4.0D * G4;
        int var91 = var21 & 255;
        int var92 = var22 & 255;
        int var93 = var23 & 255;
        int var94 = var24 & 255;
        NoiseGenerator$Grad var95 = this.getGradient(var91, var92, var93, var94);
        NoiseGenerator$Grad var96 = this.getGradient(var91 + var47, var92 + var48, var93 + var49, var94 + var50);
        NoiseGenerator$Grad var97 = this.getGradient(var91 + var51, var92 + var52, var93 + var53, var94 + var54);
        NoiseGenerator$Grad var98 = this.getGradient(var91 + var55, var92 + var56, var93 + var57, var94 + var58);
        NoiseGenerator$Grad var99 = this.getGradient(var91 + 1, var92 + 1, var93 + 1, var94 + 1);
        double var100 = 0.6D - var35 * var35 - var37 * var37 - var39 * var39 - var41 * var41;
        double var9;

        if (var100 < 0.0D)
        {
            var9 = 0.0D;
        }
        else
        {
            var100 *= var100;
            var9 = var100 * var100 * dot(var95, var35, var37, var39, var41);
        }

        double var102 = 0.6D - var59 * var59 - var61 * var61 - var63 * var63 - var65 * var65;
        double var11;

        if (var102 < 0.0D)
        {
            var11 = 0.0D;
        }
        else
        {
            var102 *= var102;
            var11 = var102 * var102 * dot(var96, var59, var61, var63, var65);
        }

        double var104 = 0.6D - var67 * var67 - var69 * var69 - var71 * var71 - var73 * var73;
        double var13;

        if (var104 < 0.0D)
        {
            var13 = 0.0D;
        }
        else
        {
            var104 *= var104;
            var13 = var104 * var104 * dot(var97, var67, var69, var71, var73);
        }

        double var106 = 0.6D - var75 * var75 - var77 * var77 - var79 * var79 - var81 * var81;
        double var15;

        if (var106 < 0.0D)
        {
            var15 = 0.0D;
        }
        else
        {
            var106 *= var106;
            var15 = var106 * var106 * dot(var98, var75, var77, var79, var81);
        }

        double var108 = 0.6D - var83 * var83 - var85 * var85 - var87 * var87 - var89 * var89;
        double var17;

        if (var108 < 0.0D)
        {
            var17 = 0.0D;
        }
        else
        {
            var108 *= var108;
            var17 = var108 * var108 * dot(var99, var83, var85, var87, var89);
        }

        return 27.0D * (var9 + var11 + var13 + var15 + var17);
    }

    private static int fastfloor(double var0)
    {
        int var2 = (int)var0;
        return var0 < (double)var2 ? var2 - 1 : var2;
    }

    private static double dot(NoiseGenerator$Grad var0, double var1, double var3)
    {
        return var0.x * var1 + var0.y * var3;
    }

    private static double dot(NoiseGenerator$Grad var0, double var1, double var3, double var5)
    {
        return var0.x * var1 + var0.y * var3 + var0.z * var5;
    }

    private static double dot(NoiseGenerator$Grad var0, double var1, double var3, double var5, double var7)
    {
        return var0.x * var1 + var0.y * var3 + var0.z * var5 + var0.w * var7;
    }

    private final NoiseGenerator$Grad getGradient(int var1, int var2)
    {
        int var3 = var1 + this.permutation[var2 & 127];
        byte var4 = this.permutation[var3 & 127];
        return grad2[var4 & 3];
    }

    private final NoiseGenerator$Grad getGradient(int var1, int var2, int var3)
    {
        int var4 = var2 + this.permutation[var3 & 127];
        int var5 = var1 + this.permutation[var4 & 127];
        byte var6 = this.permutation[var5 & 127];
        return grad3[var6 % 12];
    }

    private final NoiseGenerator$Grad getGradient(int var1, int var2, int var3, int var4)
    {
        int var5 = var3 + this.permutation[var4 & 127];
        int var6 = var2 + this.permutation[var5 & 127];
        int var7 = var1 + this.permutation[var6 & 127];
        byte var8 = this.permutation[var7 & 127];
        return grad4[var8 & 49];
    }
}
