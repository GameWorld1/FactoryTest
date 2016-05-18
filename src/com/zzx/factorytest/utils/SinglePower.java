package com.zzx.factorytest.utils;

public class SinglePower {
	// Maximum signal amplitude for 16-bit data.
    private static final float MAX_16_BIT = 32768;
    private static final float FUDGE = 0.6f;
    
	public final static double calculatePowerDb(short[] sdata, int off, int length) {
        // Calculate the sum of the values, and the sum of the squared values.
        // We need longs to avoid running out of bits.
        double sum = 0;
        double sqsum = 0;
        for (int i = 0; i < length; i++) {
            final long v = sdata[off + i];
            sum += v;
            sqsum += v * v;
        }
        
        // sqsum is the sum of all (signal+bias)², so
        //     sqsum = sum(signal²) + samples * bias²
        // hence
        //     sum(signal²) = sqsum - samples * bias²
        // Bias is simply the average value, i.e.
        //     bias = sum / samples
        // Since power = sum(signal²) / samples, we have
        //     power = (sqsum - samples * sum² / samples²) / samples
        // so
        //     power = (sqsum - sum² / samples) / samples
        double power = (sqsum - sum * sum / length) / length;

        // Scale to the range 0 - 1.
        power /= MAX_16_BIT * MAX_16_BIT;

        // Convert to dB, with 0 being max power.  Add a fudge factor to make
        // a "real" fully saturated input come to 0 dB.
        return Math.log10(power) * 10f + FUDGE;
    }
	public final static void biasAndRange(short[] sdata, int off, int samples,
            float[] out)
	{
		// Find the max and min signal values, and calculate the bias.
		short min =  32767;
		short max = -32768;
		int total = 0;
		for (int i = off; i < off + samples; ++i) {
			final short val = sdata[i];
			total += val;
			if (val < min)
				min = val;
			if (val > max)
				max = val;
		}
		final float bias = (float) total / (float) samples;
		final float bmin = min + bias;
		final float bmax = max - bias;
		final float range = Math.abs(bmax - bmin) / 2f;
		
		out[0] = bias;
		out[1] = range;
	}
    
}
