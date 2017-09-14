package me.zhaoliufeng.mylab.MusicPlayer;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("UnusedDeclaration")
public class Util {
    // private static final String TAG = Util.class.getSimpleName();

    public Util() {
        // Nothing
    }


    public static class CoolWarm {
        public int cw[] = { 0, 0 };
        public double warmRatio, brt;

        final static int maxSum = 333, maxSingleLight = 255, C = 0, W = 1;
        final static int base = maxSum - maxSingleLight;
        final static int delta = maxSingleLight - base;

        public CoolWarm(int c, int w) {
            cw[ C ] = c;
            cw[ W ] = w;
        }

        public CoolWarm(double warmRat, double brt) {
            warmRatio = warmRat;
            this.brt = brt;

            double warmDeltaRatio = ((warmRatio > 0.5) ? 1.0
                    : (0.5 - warmRatio) / 0.5);
            double coldDeltaRatio = ((warmRatio < 0.5) ? 1.0
                    : (1.0 - warmRatio) / 0.5);
            cw[ W ] = (int) ((base + delta * (1 - warmRatio)) * warmDeltaRatio * brt);
            cw[ C ] = (int) ((base + delta * warmRatio) * coldDeltaRatio * brt);
        }

        public CoolWarm SetAbsolut(double warm, double cool) {
            cw[ W ] = (int) (maxSingleLight * warm);
            cw[ C ] = (int) (maxSingleLight * cool);
            return this;
        }
    }


    public static byte[] subBytes(byte src[], int pos, int len) {
        if (src != null && pos >= 0 && len > 0 && (pos + len) <= src.length) {
            byte sub[] = new byte[ len ];
            // for (;len > 0; --len)
            // sub[len] = src[pos + len];
            System.arraycopy(src, pos, sub, 0, len);
            return sub;
        }
        return null;
    }

    public static <elemType> elemType[] subArray(elemType src[], int pos,
                                                 int len) {
        if (src != null && pos >= 0 && len > 0 && (pos + len) <= src.length) {
            @SuppressWarnings("unchecked")
            elemType sub[] = (elemType[]) new Object[ len ];

            for ( ; len > 0 ; --len)
                sub[ len ] = src[ pos + len ];
            return sub;
        }
        return null;
    }

    public static boolean byteArrayEquals(byte[] l, byte[] r) {
        if (l.length != r.length)
            return false;

        for (int idx = 0 ; idx < l.length ; ++idx)
            if (l[ idx ] != r[ idx ])
                return false;
        return true;
    }

    public static byte[] trancUtf8(String utf8s, int maxLen) {
        if (utf8s == null || maxLen <= 0)
            return null;

        int currByteLen = 0, currCharIdx = 0;
        while ( currCharIdx < utf8s.length() ) {
            currByteLen += utf8s.substring(currCharIdx, currCharIdx + 1).getBytes().length;

            if (currByteLen <= maxLen)
                ++currCharIdx;
            else
                break;
        }

        return utf8s.substring(0, currCharIdx).getBytes();
    }

    public static byte[] toBytes(long l) {
        byte[] bytes = new byte[ 8 ];
        for (int idx = 7 ; idx >= 0 ; --idx) {
            bytes[ idx ] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return bytes;
    }

    public static byte[] toBytes(int i) {
        return new byte[]{ (byte) ((i >> 24) & 0xFF), (byte) ((i >> 16) & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) (i & 0xFF) };
    }

    public static byte[] toBytes(short s) {
        return new byte[]{ (byte) ((s >> 8) & 0xFF), (byte) (s & 0xFF) };
    }

    public static byte[] toBytesRvs(long l) {
        byte[] bytes = new byte[ 8 ];
        for (int idx = 0 ; idx < 0 ; ++idx) {
            bytes[ idx ] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return bytes;
    }

    public static byte[] toBytesRvs(int i) {
        return new byte[]{ (byte) (i & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) ((i >> 16) & 0xFF), (byte) ((i >> 24) & 0xFF), };
    }

    public static byte[] toBytesRvs(short s) {
        return new byte[]{ (byte) (s & 0xFF), (byte) ((s >> 8) & 0xFF), };
    }

    public static int sizeof(byte checksum) {
        return 1;
    }

    public static int sizeof(short checksum) {
        return 2;
    }

    public static int sizeof(int checksum) {
        return 4;
    }

    public static int sizeof(long checksum) {
        return 8;
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    public static class ARGB {
        /*
         * Bits:   | 24 - 31 | 16 - 23 | 8 - 15 | 0 - 7 |
         * Means:  | alpha   |   red   |  green |  blue |
         * Ranges: | 0 - 255 | 0 - 255 |0 - 255 |0 - 255|
         */
        public int argb = 0;

        @SuppressWarnings("unused")
        private static final int MAX_A = 255, MAX_R = 255, MAX_G = 255, MAX_B = 255;

        public final int argb() {
            return argb;
        }

        public final ARGB argb(int c) {
            argb = c;
            return this;
        }

        public final static int
        A(int argb) {
            return (argb >> 24) & 0xFF;
        }

        public final int a() {
            return (argb >> 24) & 0xFF;
        }

        public final ARGB a(int a) {
            return argb((argb & ~0xFF000000) | ((a & 0xFF) << 24));
        }

        public final static int
        R(int argb) {
            return (argb >> 16) & 0xFF;
        }

        public final int r() {
            return (argb >> 16) & 0xFF;
        }

        public final ARGB r(int r) {
            return argb((argb & ~0xFF0000) | ((r & 0xFF) << 16));
        }

        public final static int
        G(int argb) {
            return (argb >> 8) & 0xFF;
        }

        public final int g() {
            return (argb >> 8) & 0xFF;
        }

        public final ARGB g(int g) {
            return argb((argb & ~0xFF00) | ((g & 0xFF) << 8));
        }

        public final static int
        B(int argb) {
            return (argb >> 0) & 0xFF;
        }

        public final int b() {
            return (argb >> 0) & 0xFF;
        }

        public final ARGB b(int b) {
            return argb((argb & ~0xFF) | (b & 0xFF));
        }

        public final long toAHSB() {
            return toAHSB(argb);
        }

        public final static long toAHSB(int argb) {
            int r = R(argb), g = G(argb), b = B(argb);
            int max = (r > g ? (r > b ? r : (g > b ? g : b)) : (g > b ? g : b));
            int min = (r < g ? (r < b ? r : (g < b ? g : b)) : (g < b ? g : b));

            int fullSat = max - min;
            int hue_calc = 0;
            if (max == r)
                hue_calc = ((g - b) / fullSat * 60 + 360) % 360;
            else if (max == g)
                hue_calc = ((b - r) / fullSat * 60 + 120);
            else
                hue_calc = ((r - g) / fullSat * 60 + 240);

            int hue = max == min ? 0 : hue_calc;
            int sat = max == 0 ? 0 : (fullSat) * 100 / max;
            int brt = max;
            return new AHSB().a(A(argb)).h(hue).s(sat).b(brt).ahsb;
        }

    }

    public static class AHSB {
        /*
         * bits:	| 40 - 31 | 31 - 16 | 	15 - 8 	 | 	  7 - 0   |
         * means:	|  Alpha  |    Hue  | Saturation | Brightness |
         * range:	| 0 - 255 | 0 - 360 |   0 - 255  |  0 - 255   |
         */
        public long ahsb;

        @SuppressWarnings("unused")
        private static final int MAX_A = 255, MAX_H = 360, MAX_S = 255, MAX_B = 255;

        public final long ahsb() {
            return ahsb;
        }

        public final AHSB ahsb(long c) {
            ahsb = c;
            return this;
        }

        public final int A(int argb) {
            return (int) ((ahsb >> 32) & 0xFFL);
        }

        public final int a() {
            return (int) ((ahsb >> 32) & 0xFFL);
        }

        public final AHSB a(int a) {
            return ahsb((ahsb & ~0xFF00000000L) | ((a & 0xFF) << 32));
        }

        public final int H(int argb) {
            return (int) ((ahsb >> 16) % 360);
        }

        public final int h() {
            return (int) ((ahsb >> 16) & 0xFFL);
        }

        public final AHSB h(int a) {
            return ahsb((ahsb & ~0xFFFF0000L) | ((a % 360) << 16));
        }

        public final int S(int argb) {
            return (int) ((ahsb >> 8) & 0xFFL);
        }

        public final int s() {
            return (int) ((ahsb >> 8) & 0xFFL);
        }

        public final AHSB s(int a) {
            return ahsb((ahsb & ~0xFF00L) | ((a & 0xFF) << 8));
        }

        public final int B(int argb) {
            return (int) ((ahsb >> 0) & 0xFFL);
        }

        public final int b() {
            return (int) ((ahsb >> 0) & 0xFFL);
        }

        public final AHSB b(int a) {
            return ahsb((ahsb & ~0xFFL) | ((a & 0xFF) << 0));
        }

        public final int toARGB() {
            return toARGB(ahsb);
        }

        public static int toARGB(long ahsb) {
            AHSB c = new AHSB().ahsb(ahsb);
            int alpha = c.a(),
                    hue = c.h(),
                    saturation = c.s(),
                    brightness = c.b();

            int max = brightness,
                    min = brightness * saturation / MAX_S,
                    middle = (hue % (MAX_H / 6)) / (MAX_H / 6) * MAX_S;

            int scope = hue / (MAX_H / 3);

            final int[][] rgb = {
                    { max, middle, min },
                    { middle, max, min },
                    { min, max, middle },
                    { min, middle, max },
                    { middle, min, max },
                    { max, min, middle },
            };

            return new ARGB().a(alpha).r(rgb[ scope ][ 0 ]).g(rgb[ scope ][ 1 ]).b(rgb[ scope ][ 2 ]).argb;
        }
    }

    public static class UIColor {
        private final static int
                H = 0, S = 1, B = 2, A = 3,
                RGBA_SECTION = 0xFF;
        double[] hsba = new double[ 4 ];     // All: [0.0, 1.0]
        long rgba = 0;                 // all to 0, [0, 255]

        @Deprecated
        public UIColor() {
        }   // Only used for ISRUtil.

//	    public UIColor(int argb)
//	    {
//	        this(red(argb), green(argb), blue(argb), alpha(argb));
//	    }

        /*
         * r,g,b => [0, 255]
         */
        public UIColor(int r, int g, int b) {
            this(r, g, b, 0xFF);
        }

        /*
         * r,g,b,a => [0, 255]
         */
        public UIColor(int r, int g, int b, int a) {
            rgba = Color.argb(a, r, g, b);

            float[] hsba_f = new float[ 4 ];
//            Color.RGBToHSV(r, g, b, hsba_f);
            hsba_f[ H ] /= 360.0;
            for (int idx = 0 ; idx < A ; ++idx)
                hsba[ idx ] = hsba_f[ idx ];

            hsba[ A ] = (a % RGBA_SECTION) / (float) RGBA_SECTION;

            if (hsba[ A ] > 1.0) hsba[ A ] = 1.0f;
            else if (hsba[ A ] < 0.0) hsba[ A ] = 0.0f;
        }

        /*
         * h,s,b,a => [0.0, 1.0]
         */
        public UIColor(double h, double s, double b) {
            this(h, s, b, 1.0);
        }

        public UIColor(double h, double s, double b, double a) {
            fromHSBA(h, s, b, a);
        }

        public void fromHSBA(double h, double s, double b, double a) {
            hsba[ H ] = h;
            hsba[ S ] = s;
            hsba[ B ] = b;
            hsba[ A ] = a;

            for (int idx = 0 ; idx < hsba.length ; idx++)
                if (hsba[ idx ] > 1.0f)
                    hsba[ idx ] = 1.0f;
                else if (hsba[ idx ] < 0.0f)
                    hsba[ idx ] = 0.0f;

            rgba = Color.HSVToColor((int) (hsba[ A ] * RGBA_SECTION),
                    new float[]{ (float) (h * 360.0), (float) s, (float) b });
        }

        public int R() {
            return Color.red((int) rgba);
        }

        public int G() {
            return Color.green((int) rgba);
        }

        public int B() {
            return Color.blue((int) rgba);
        }

        public int Ai() {
            return Color.alpha((int) rgba);
        }

        public int ARGB() {
            return (int) rgba;
        }

        public double hue() {
            return hsba[ H ];
        }

        public double sat() {
            return hsba[ S ];
        }

        public double brt() {
            return hsba[ B ];
        }

        public double Af() {
            return hsba[ A ];
        }

        // public UIColor hue(double h)
        // { hsba[H] = h; return this; }
        // public UIColor sat(double s)
        // { hsba[S] = s; return this; }
        // public UIColor brt(double b)
        // { hsba[B] = b; return this; }
        // public UIColor Af(double a)
        // { hsba[A] = a; return this; }

        public String toString() {
            return String.format(Locale.getDefault(),
                    "A:%4f / R: %x G:%x B:%x / H:%4f S:%4f B:%4f", Af(), R(),
                    G(), B(), hue(), sat(), brt());
        }

//        @Override
//        public void saveClass(Map<String, Object> map) {
//            // ISRUtil.save(hsba, "hsba", map);
//            ISRUtil.save(hsba[H], "H", map);
//            ISRUtil.save(hsba[S], "S", map);
//            ISRUtil.save(hsba[B], "B", map);
//            ISRUtil.save(hsba[A], "A", map);
//        }
//
//        @Override
//        public Object restoreClass(Map<String, Object> map) {
//            // hsba = ISRUtil.restore(hsba, "hsba", map);
//            hsba[H] = ISRUtil.restore(hsba[H], "H", map);
//            hsba[S] = ISRUtil.restore(hsba[S], "S", map);
//            hsba[B] = ISRUtil.restore(hsba[B], "B", map);
//            hsba[A] = ISRUtil.restore(hsba[A], "A", map);
//            fromHSBA(hsba[H], hsba[S], hsba[B], hsba[A]);
//
//            return this;
//        }

        public static int interpolate(int startColor, int endColor, float pos) {
            int startA = Color.alpha(startColor);
            int startR = Color.red(startColor);
            int startG = Color.green(startColor);
            int startB = Color.blue(startColor);

            int endA = Color.alpha(endColor);
            int endR = Color.red(endColor);
            int endG = Color.green(endColor);
            int endB = Color.blue(endColor);

            int interA = (int) (startA + (endA - startA) * pos);
            int interR = (int) (startR + (endR - startR) * pos);
            int interG = (int) (startG + (endG - startG) * pos);
            int interB = (int) (startB + (endB - startB) * pos);

            return Color.argb(interA, interR, interG, interB);
        }
    }


    /*
     * Queue for apps.
     */
    public static class CycleQueue<TYPE> {
        final public static int DEF_BUF_LEN = 32;

        int bufLen = DEF_BUF_LEN;
        // TYPE[] buffer = null;
        ArrayList<TYPE> buffer = null;

        public CycleQueue(int maxBufLen) {
            if (maxBufLen < 0)
                throw new IllegalArgumentException(
                        "CycleQueue must hava a positive maxBufLen! Not "
                                + maxBufLen);

            bufLen = maxBufLen;
            // buffer = new TYPE[bufLen];
            buffer = new ArrayList<TYPE>(bufLen);
        }
    }

    /*
     * Sequence controller
     */
    public static class Gap {
        private long lastProc = 0, miniGap = 0;

        public Gap(long msMiniGap) {
            if (msMiniGap < 0)
                throw new IllegalArgumentException(Gap.class.getSimpleName()
                        + " Must init with a msGap >= 0");

            miniGap = msMiniGap;
            lastProc = System.currentTimeMillis() - miniGap;
        }

        public void MiniGap(long newGap) {
            if (miniGap > 0)
                miniGap = newGap;
        }

        public long MiniGap() {
            return miniGap;
        }

        synchronized public boolean peekNext() {
            return (System.currentTimeMillis() - lastProc) >= miniGap;
        }

        synchronized public boolean passedNext() {
            long currMS = System.currentTimeMillis();
            if (currMS - lastProc >= miniGap) {
                lastProc = currMS;
                return true;
            }
            return false;
        }

        synchronized public void updateToNow() {
            lastProc = System.currentTimeMillis();
        }

        synchronized public void reset() {
            lastProc = -miniGap;
        }

        // synchronized public boolean passedDaily() {
        // long currMS = System.currentTimeMillis();
        //
        // final long msPerDay = 24 * 60 * 60 * 1000;
        // final long msDeltaUTC = System...
        // new Date().
        // if (currMS / msPerDay != lastProc / msPerDay)
        // {
        // lastProc = currMS;
        // return true;
        // }
        // return false;
        // }
    }

    /*
     * TimerStamp for debug.
     */
    public static class TimerStamp {
        public final static int DEF_STAMP_CNT = 3;

        long stamps[] = null;
        int stampCnt = 0;
        int currentStamp = 0;

        public TimerStamp(int stampCnt) {
            if (stampCnt <= 0)
                throw new IllegalArgumentException(
                        "TimerStamp's stampCnt must > 0! Not " + stampCnt);

            this.stampCnt = stampCnt;
            currentStamp = 0;
            stamps = new long[ stampCnt ];
        }

        private void MoveNext() {
            for (int idx = stampCnt - 1 ; idx > 0 ; --idx)
                stamps[ idx ] = stamps[ idx - 1 ];
        }

        public TimerStamp() {
            this(DEF_STAMP_CNT);
        }

        public long Start() {
            return Mark();
        }

        public long Mark() {
            MoveNext();
            return stamps[ 0 ] = System.currentTimeMillis() - stamps[ 0 ];
        }

        public void Reset() {
            stamps = new long[ stampCnt ];
        }

        public long Lap(int idx) {
            if (0 <= idx && idx < stampCnt)
                return stamps[ idx ];
            return -1;
        }

        public long[] Laps() {
            long[] ret = new long[ stampCnt - 1 ];
            for (int idx = 0 ; idx < stampCnt ; ++idx)
                ret[ idx ] = stamps[ idx ];
            return ret;
        }

        public long LastLap() {
            return stamps[ 0 ];
        }

        // public long Average(int fisrt, int last)
        // { return 0; }
        // public long Average(int last)
        // { return 0; }
        // public long Average()
        // { return 0; }
        //
        // public long Sum(int fisrt, int last)
        // { return 0; }
        // public long Sum(int last)
        // { return 0; }
        // public long Sum()
        // { return 0; }
    }

    /*
     * Singleton
     */
    /*
     * public static class Single<Type> { private Type single = null;
     * 
     * public Single() { }
     * 
     * public Single(Type t) { single = t; }
     * 
     * public Type S() { if (single == null) synchronized (this) { if (single ==
     * null) single = new Type(); } return single; } } //
     */

    public static Map<String, Object> JsonStrToMap(String json, String encoding) throws JSONException {
        HashMap<String, Object> map = new HashMap<>();
        JSONObject jo = new JSONObject(json);
        return JsonToMap(map, jo);
    }

    public static Map<String, Object> JsonToMap(
            HashMap<String, Object> hashMap, JSONObject json)
            throws JSONException {
        if (hashMap != null && !JSONObject.NULL.equals(json)) {

            for (Iterator<String> it = json.keys(); it.hasNext() ; ) {
                String key = it.next();
                Object value = json.get(key);

                if (value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                } else if (value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }

                hashMap.put(key, value);
            }
        }

        return hashMap;
    }

    public static Map<String, Object> toMap(JSONObject object)
            throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while ( keysItr.hasNext() ) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }

            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0 ; i < array.length() ; i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public final static class Sync {
        Lock lock = new ReentrantLock();
        Condition cond = lock.newCondition();

        public Sync() {
            lock.lock();
        }

        public boolean Join() {
            try {
                cond.await();
                return true;
            } catch (InterruptedException e) {
//                CoreData.PushErrReport(e);
                return false;
            }
        }

        public boolean Join(long millseconds) {
            try {
                return cond.await(millseconds, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
//                CoreData.PushErrReport(e);
                return false;
            }
        }

        public void Synced() {
            lock.lock();
            cond.signal();
            lock.unlock();
        }

        public void SyncedAll() {
            lock.lock();
            cond.signalAll();
            lock.unlock();
        }

    }

    /**
     * Collect device info.
     *
     * @param infos
     */
    public static void CollectDeviceInfo(Map<String, String> infos) {
        final String TAG = "CollectDeviceInfo";
//        try {
//            Application ctx = CoreData.appContext;
//            PackageManager pm = ctx.getPackageManager();
//            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
//                PackageManager.GET_ACTIVITIES);
//            if (pi != null) {
//                String versionName = pi.versionName == null ? "null"
//                    : pi.versionName;
//                String versionCode = pi.versionCode + "";
//                infos.put("versionName", versionName);
//                infos.put("versionCode", versionCode);
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.e(TAG, "an error occured when collect package info", e);
//        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }


    // public static Map<String, Object> GsonToMap (
    // HashMap<String, Object> hashMap, JSONObject json) {
    // Map<String, Object> retMap = new Gson().fromJson(jsonString, new
    // TypeToken<HashMap<String, Object>>() {}.getType());
    // }

//	private static final int BLACK = 0xff000000;

//	public static Bitmap getQrCodeImg(BitMatrix matrix) {
//		int   width  = matrix.getWidth();
//		int   height = matrix.getHeight();
//		int[] pixels = new int[ width * height ];
//		for (int y = 0 ; y < height ; y++) {
//			for (int x = 0 ; x < width ; x++) {
//				if (matrix.get(x, y)) {
//					pixels[ y * width + x ] = BLACK;
//				}
//			}
//		}
//		Bitmap bitmap = Bitmap.createBitmap(width, height,
//				Bitmap.Config.ARGB_8888);
//		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//		return bitmap;
//	}


    public synchronized static int getAllocationAddress(List<Integer> mList) {
        int number = 0;
        label:
        for (int x = 1 ; x <= 255 ; x++) {
            for (Integer i : mList) {
                if (mList.contains(x)) {
                    break;
                } else {
                    number = x;
                    break label;
                }
            }
        }
        return number;
    }

    public static int getArrayMax(int[] array) {
        int number = array[ 0 ];
        for (int x = 0 ; x < array.length ; x++) {
            if (array[ x ] >= number) {
                number = array[ x ];
            }
        }
        return number;
    }


    private static final int BLACK = 0xff000000;

    private static final int GREEN = 0xff00FF00;
}
