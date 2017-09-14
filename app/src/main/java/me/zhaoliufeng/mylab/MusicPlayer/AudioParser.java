package me.zhaoliufeng.mylab.MusicPlayer;

import android.util.Log;


public class AudioParser {
    private static final String TAG = AudioParser.class.getSimpleName();
    boolean isInited;
    double hisDB[] = new double[20];
    int currIdx, lastTopIdx, lastLowIdx;
    double lastTop, lastAvrg, section, base;
    double delta, avrgDelta, powerDelta, deltaSection, deltaDelta, deltaRatio, standrad_deviation, variance;

    boolean isLow, isHigh;

    final double baseHueLine = 0.5;

    public void ProcessDB(double db) {
        int hisDBLen = hisDB.length;
        currIdx = (currIdx + 1) % hisDBLen;
        hisDB[currIdx] = db;

        double sum = 0;
        for (int idx = 0; idx < hisDBLen; ++idx) {
            if (hisDB[idx] > hisDB[lastTopIdx])
                lastTopIdx = idx;
            if (hisDB[idx] < hisDB[lastLowIdx])
                lastLowIdx = idx;

            sum += hisDB[idx];
        }

        isHigh = (lastTopIdx == (currIdx + hisDBLen - 3) % hisDBLen);
        isLow = (lastLowIdx == (currIdx + hisDBLen - 3) % hisDBLen);

        lastTop = hisDB[lastTopIdx];
        lastAvrg = sum / hisDBLen;
        // update section & base
        section = 2 * (lastTop - lastAvrg);
        base = lastTop - section;
        // if (base < 0) base = 0;
        // update this delta
        standrad_deviation = lastAvrg / (section / 2);
        variance = (1.0 / standrad_deviation / standrad_deviation);
        delta = (db - lastAvrg) / section /* /standrad_deviation */;
        powerDelta = delta * delta * delta;
    }

    private static AudioParser deltaDP = new AudioParser();     // TODO!

    void ParseDB(double db) {
        ProcessDB(db);

        deltaDP.ProcessDB(delta);
        avrgDelta = deltaDP.lastAvrg;
        deltaDelta = deltaDP.delta;
        deltaSection = deltaDP.deltaSection;
        deltaRatio = (delta - avrgDelta);
    }


    public void ParseDBOld(double db) {
        final int sampleCnt = 20, currWeight = 1;
        // update average
        lastAvrg = ((lastAvrg == 0) ? db : (db * currWeight + lastAvrg * (sampleCnt - currWeight)) / sampleCnt);
        // update top
        if (lastTop == 0) lastTop = db;
        final double attenuation = 0.05;
        // (lastTop < db) ? (lastTop = db) : (lastTop -= attenuation * lastTop);       // Cause trimble.
        lastTop = ((lastTop < db) ? db : (lastTop - attenuation * lastTop));
        // update section & base
        section = 2 * (lastTop - lastAvrg);
        base = lastTop - section;
        if (base < 0) base = 0;
        // update this delta
        standrad_deviation = lastAvrg / (section / 2);
        variance = (1.0 / standrad_deviation / standrad_deviation);
        delta = (db - lastAvrg) / section /* /standrad_deviation */;
        powerDelta = delta * delta * delta;
    }

    public Util.UIColor CalcHSBByDB(double db) {
        ParseDB(db);
        double hue, sat, brt;

        final double centerHue = 0.0;

        final int STYPE_STABLE = 0, STYPE_STABLE_FLEX = 1, STYPE_FLEX = 2;
        final int stype = STYPE_STABLE;
        switch (stype) {
            case STYPE_STABLE:
                // stable
                hue = lastAvrg / lastTop - 0.5 + deltaRatio * 1.2;
                sat = 1.0;
                brt = 0.45 + deltaRatio;  // 0.2 + *hue*delta

//                hue = lastAvrg/lastTop;
//                sat = 1.0;
//                brt = 0.2 + delta;  // 0.2 + *hue*delta
                break;

            case STYPE_STABLE_FLEX:
            default:
                // stable + flex
                hue = (lastAvrg - lastTop / 2) / lastTop + delta / standrad_deviation;
                sat = 1.0;
                brt = 0.2 + hue * delta;
                break;

            case STYPE_FLEX:
                // flex
                hue = baseHueLine + section / lastAvrg + delta / standrad_deviation * 2;
                sat = 1.0;
                brt = 0.2 + delta;
                break;
        }

        hue = Math.random();

        hue += centerHue - 0.5;
        if (hue > centerHue + 0.5) hue = centerHue + 0.5;
        if (hue < centerHue - 0.5) hue = centerHue - 0.5;
        if (hue > 1.0) hue -= 1.0;
        if (hue < 0.0) hue += 1.0;


        Log.i(TAG, "CalcHSBByDB: db/" + db + " => top:" + lastTop + "/ avrg:" + lastAvrg
                + "/ sec:" + section + "/ delta:" + delta + "/ std_dev:" + standrad_deviation
                + "\n/ hue:" + hue + "/ brt:" + brt);

        return new Util.UIColor(hue, sat, brt);
    }
}
