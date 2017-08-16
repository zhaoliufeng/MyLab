package me.zhaoliufeng.mylab;

/**
 * Created by zxd on 2016/3/8.
 */
public class SysConfig {
    public static final String
            VOICE_SET = "voice_set",
            VOICE_SWITCH = "voice_switch",
            MESH = "mesh",
            MESHADDRESS = "mCurrMeshAddress",
            MESHPASSWROD = "2846",
            MESHNAME = "Fulife",
            PAGE_TYPE = "page_type",
            CON_STATUS = "con_status";

    public static final int MIN_SEND_GAP = 80;

    //声音的阈值函数
    public static final int VOICE_THRESHOLD = 10;

    //默认的数据
    public static final String DEFAULTACCOUNT = "MeshLamp";
    public static final String DEFAULTACCOUNTPASSWORD = "123";
    public static final String DEFAULTMESHNAME = "DefaultMesh";

    public static final String DEFAULTNAME = "light";
    /*
    * Mesh的数据库的字段
    * */
    public static final String
            MESH_DEVICE_TABLE_NAME = "device_table_name",
            MESH_GROUP_TABLE_NAME = "group_table_name",
            MESH_CREATE_TIME = "table_time",
            MESH_FACTORY_NAME = "factory_name",
            MESH_FACTORY_PASSWORD = "factory_password",
            MESH_GATEWAY_ID = "gatewayId",
            MESH_SCENE_DATA = "mesh_scene_data",
            MESH_ALARM_DATA = "mesh_alarm_data",
            MESH_DAY_NIGHT = "day_night",
            MESH_ACCOUNT = "account",
            MESH_NET_ID = "net_id",
            MESH_REAL_ACCOUNT = "real_password",
            MESH_CUSTOM_COLOR_LUMP = "color_lump",
            MESH_PASSWORD = "password";

    //设备的数据库的字段
    public static final String DEV_NAME = "name",
            DEV_ID = "id",
            DEV_NET_ID = "NetId",
            DEV_NET_PID = "Net_pid",
            CHANGE_VALUE = "changevalue",
            DEV_MAC = "macaddress",
            DEV_DAY_NIGHT = "dayandnight",
            DEV_TYPE = "type",
            DEV_GROUP = "grouptext",
            DEV_SCENE = "scenetext",
            DEV_ALARM = "alarmtext",
            DEV_RED = "red",
            DEV_GREEN = "green",
            DEV_BLUE = "blue",
            DEV_WARM = "warm",
            DEV_COLD = "cold",
            DEV_FIRMWARE = "firmware",
            DEV_BRIGHTNESS = "brightness";
    //群组的数据库的字段
    public static final String GROUP_NAME = "GName",
            GROUP_ID = "GId",
            GROUP_SCENE = "GScene",
            GROUP_ALARM = "GAlarm",
            GROUP_ICON = "GroupIcon",
            GROUP_NET_ID = "GNetId",
            GROUP_ALARM_SCENE = "GAlarmScene",
            GROUP_DEV = "GDevice";

    //场景的数据库的字段
    public static final String SCENE_NAME = "Sname",
            SCENE_HASBEEN_SET = "Scehasbeenset",
            SCENE_ID = "Sid",
            SCENE_RED = "Sred",
            SCENE_LUMINANCE = "Sceneluminance",
            SCENE_GREEN = "Sgreen",
            SCENE_SUB_ID = "SSub_id",
            SCENE_WARM = "Swarm",
            SCENE_COOL = "Scool",
            SCENE_BLUE = "Sblue",
            SCENE_IS_OPEN = "Sisopen";

    //闹钟的数据库字段
    public static final String ALARM_NAME = "desc",
            ALARM_TASK_POSITION = "SceId",
            ALARM_ID = "id",
            ALARM_MODE = "mode",
            ALARM_OPEN = "status",
            ALARM_WEEK_NUM = "Dur",
            ALARM_HOURS = "hour",
            ALARM_MINUTES = "Min",
            ALARM_SECOND = "Sec",
            ALARM_GROUP_ID = "GroId",
            ALARM_DAY = "day",
            ALARM_MONTHS = "Mon",
            ALARM_TYPE = "type",
            ALARM_YEAR = "year";

    //用户数据
    public static final String USER_ACCOUNT = "account",
            USER_PASSWORD = "password",
            USER_COUNTRY = "country",
            USER_COUNTRY_CODE = "country_code",
            USER_NICK_NAME = "nick_name",
            USER_ICON_PATH = "icon_path",
            USER_LAST_MESH = "recently_mesh",
            USER_MESH_TABLE = "mesh_table",
            USER_LOGIN_TIME = "login_time",
            USER_CREATE_TIME = "create_time";


    //视图类型
    public static final int ALLDEVICEVIEW = 0;
    public static final int DEVICEVIEW = 1;

    public static final int DEFAULTTYPE = 0xA0FF;
    public static final int FORM_DEFAULTTYPE = 0xFF;


    /*
    * 用户
    * */
    public static final String USER = "user";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String COUNTRYCODE = "countryCode";
    public static final String COUNTRYNAME = "countryName";

    /*
    * 方式---忘记密码/重置密码的区别
    * */
    public static final String LOGIN_WAY = "logway";
    public static final String ID = "id";
    public static final byte OPCODE_SPECIAL_FUNCTION = (byte) 0xFF;


    public static String LOCAL_RES = "local_data";
    public static String LOCAL_TYPE = "local_type";
    public static final int LOCAL_TYPE_ENGLISH = 1;
    public static final int LOCAL_TYPE_CHINESE = 2;
    public static final int LOCAL_TYPE_HK = 3;
    public static final int LOCAL_TYPE_NEDERLAND = 4;


    //语言
    //HK
    public static final String HK = "hongkong";
    //Chinese
    public static final String CHINESE = "chinese";
    //English
    public static final String ENGLISH = "english";
    //Nederland
    public static final String NEDERLAND = "nederland";

    /*
    * 
    * 昼夜节律对象
    * */
    public static final String DAYSTARTHOURS = "dayStartHours";
    public static final String DAYSTARTMINUTE = "dayStartMinute";
    public static final String DAYDURTIME = "dayDurTime";
    public static final String NIGHTSTARTHOURS = "nightStartHours";
    public static final String NIGHTSTARMINUTE = "nightStartMinute";
    public static final String NIGHTDURTIME = "nightDurTime";
    public static final String DAYISOPEN = "DayisOpen";
    public static final String NIGHTISOPEN = "nightisOpen";


    /*
    *操作码部分 
    * 
    * */
    //控制开关
    public static final byte DEVICESWITCH = (byte) 0xD0;
    //控制色彩
    public static final byte DEVICECOLOR = (byte) 0xE2;
    //控制亮度
    public static final byte DEVICELUMINANCE = (byte) 0xD2;
    //设置群组
    public static final byte GROUPSET = (byte) 0xD7;
    //请求群组信息
    public static final byte GROUPREQUEST = (byte) 0xDD;
    //获取固件版本号
    public static final byte GETFIRMAIRE = (byte) 0xC7;
    //踢出网络
    public static final byte KICK_OUT = (byte) 0xE3;
    //添加或者删除场景
    public static final byte OPCODE_ADD_SCENE = (byte) 0xEE;
    //加载场景
    public static final byte OPCODE_LOAD_SCENE = (byte) 0xEF;
    //开灯关灯
    public static final byte OPCODE_SWITCH_LIGHT = (byte) 0xD0;
    //时间设置
    public static final byte OPCODE_SETTING_TIME = (byte) 0xE4;
    //添加闹钟
    public static final byte OPCODE_ADD_ALARM = (byte) 0xE5;
    //获取闹钟
    public static final byte OPCODE_GET_ALARM = (byte) 0xE6;
    //传感器
    public static final byte OPCODE_SENSOR_OPERATION = (byte) 0xF0;
    //传感器和设备的绑定
    public static final byte OPCODE_SENSOR_DEVICE = (byte) 0xF1;
    //读取传感器的值
    public static final byte OPCODE_SENSOR_VALUE = (byte) 0xF3;
    //遥控器的配置操作码
    public static final byte OPCODE_CONCTRL_VALUE = (byte) 0xF4;
    //设备类型
    /*
     *Light 灯具类  
     */

    //灯带
    public static final int LIGHT_LIGHTSWITH = 0xA000;
    //吊灯
    public static final int LIGHT_DROPLIGHT = 0xA001;
    //轨道灯
    public static final int LIGHT_TRACK_LIGHTING = 0xA002;
    //落地灯
    public static final int LIGHT_FLOOR_LAMP = 0xA003;
    //面板灯
    public static final int LIGHT_PANEL_LIGHT = 0xA004;
    //台灯
    public static final int LIGHT_TABLE_LAMP = 0xA005;
    //天花灯
    public static final int LIGHT_CEILING_LAMP = 0xA006;
    //吸顶灯
    public static final int LIGHT_VLZ_B = 0xA007;
    //两路吸顶灯
    public static final int LIGHT_TWO_VLZ_B = 0xA407;
    //A型球泡灯
    public static final int LIGHT_A_BULB = 0xA008;
    //B型蜡烛灯
    public static final int LIGHT_B_CANDLE_LIGHT = 0xA009;
    //BR-灯
    public static final int LIGHT_BR_LIGHT = 0xA00A;
    //G型球泡灯
    public static final int LIGHT_G_BULB = 0xA00B;
    //G10射灯
    public static final int LIGHT_GU10_LED_SPOT_LIGHT = 0xA00C;
    //正白射灯
    public static final int LIGHT_WHITE_LED_SPOT_LIGHT = 0xA50C;
    //MR16射灯
    public static final int LIGHT_MR16_LED_SPOT_LIGHT = 0xA00D;
    //PAR灯
    public static final int LIGHT_PAR_LIGHT = 0xA00E;
    //T5-T8灯管
    public static final int LIGHT_T5_T8_TUBE = 0xA020;

    //情景灯
    public static final int LIGHT_SCENE_LAMP = 0xA010;
    //斗胆灯
    public static final int GIMBAL_SPOT_LIGHT = 0xA011;
    //T5-T8一体灯
    public static final int LIGHT_T5_T8_INTEGRATION = 0xA021;
    //筒灯
    public static final int LIGHT_CANISTER_LAMP = 0xA022;
    //两路筒灯
    public static final int LIGHT_TWO_CANISTER_LIGHT = 0xA422;
    //网关设备
    public static final int OTHER_DEVICE_GATEWAY = 0xC100;
    public static String TEST_DATA = "Test_Data";


    //功能设备的形态
    public static class DeviceForm {

        //灯带
        public static final int FORM_LIGHT_LIGHTSWITH = 0x00;
        //吊灯FORM_
        public static final int FORM_LIGHT_DROPLIGHT = 0x01;
        //轨道灯FORM_
        public static final int FORM_LIGHT_TRACK_LIGHTING = 0x02;
        //落地灯FORM_
        public static final int FORM_LIGHT_FLOOR_LAMP = 0x03;
        //面板灯FORM_
        public static final int FORM_LIGHT_PANEL_LIGHT = 0x04;
        //台灯FORM_
        public static final int FORM_LIGHT_TABLE_LAMP = 0x05;
        //天花灯FORM_
        public static final int FORM_LIGHT_CEILING_LAMP = 0x06;
        //吸顶灯FORM_
        public static final int FORM_LIGHT_VLZ_B = 0x07;
        //A型球泡灯FORM_
        public static final int FORM_LIGHT_A_BULB = 0x08;
        //B型蜡烛灯FORM_
        public static final int FORM_LIGHT_B_CANDLE_LIGHT = 0x09;
        //BR-灯FORM_
        public static final int FORM_LIGHT_BR_LIGHT = 0x0A;
        //G型球泡灯FORM_
        public static final int FORM_LIGHT_G_BULB = 0x0B;
        //G10射灯FORM_
        public static final int FORM_LIGHT_GU10_LED_SPOT_LIGHT = 0x0C;
        //MR16射灯FORM_
        public static final int FORM_LIGHT_MR16_LED_SPOT_LIGHT = 0x0D;
        //情景灯FORM_
        public static final int FORM_LIGHT_SCENE_LAMP = 0x10;
        //PAR灯FORM_
        public static final int FORM_LIGHT_PAR_LIGHT = 0x0E;
        //T5-T8灯管FORM_
        public static final int FORM_LIGHT_T5_T8_TUBE = 0x20;
        //T5-T8一体灯FORM_
        public static final int FORM_LIGHT_T5_T8_INTEGRATION = 0x21;
        //筒灯FORM_
        public static final int FORM_LIGHT_CANISTER_LAMP = 0x22;
        //两路筒灯FORM_
        public static final int FORM_LIGHT_TWO_CANISTER_LIGHT = 0x22;
        //风雨传感器
        public static final int FORM_SENSOR_WIND_AND_RAIN = 0x00;
        //煤气传感器FORM_
        public static final int FORM_SENSOR_GAS = 0x01;
        //门磁传感器FORM_
        public static final int FORM_SENSOR_DOOR_MAGNETIC = 0x02;
        //人体红外线传感器FORM_
        public static final int FORM_SENSOR_INFRARED_BODY = 0x03;
        //水浸传感器FORM_
        public static final int FORM_SENSOR_WATER_OUT = 0x04;
        //温度传感器FORM_
        public static final int FORM_SENSOR_TEMPERATURE = 0x05;
        //烟雾传感器FORM_
        public static final int FORM_SENSOR_SMOKE = 0x06;
        //照度传感器FORM_
        public static final int FORM_SENSOR_ILLUMINATION = 0x07;
        //斗胆灯
        public static final int FORM_GIMBAL_SPOT_LIGHT = 0x11;
    }

    /**
     * * 设备的大类
     **/

    public static class DeviceChannel {
        /*
        * A类功能性通道
        * */
        //WC两路设备
        public final static int LIGHT_WC_TWO_CHANNEL = 0xA4;
        //RGBWC五路
        public final static int LIGHT_FIVE_RGBWC_CHANNEL = 0xA0;
        //RGBW
        public final static int LIGHT_FOUR_RGBW_CHANNEL = 0xA1;
        //RGBC
        public final static int LIGHT_FOUR_RGBC_CHANNEL = 0xA2;
        //RGB
        public final static int LIGHT_THREE_RGB_CHANNEL = 0xA3;
        //W
        public final static int LIGHT_ONE_W_CHANNEL = 0xA5;
        //C
        public final static int LIGHT_ONE_C_CHANNEL = 0xA6;

        /*
         * B类传感器通道
         * */
        //B类传感器设备
        public final static int SENSOR_DEVICE = 0xB0;

        /*
         * C类非功能性设备
         * */
        //遥控器
        public final static int DEVICE_REMOTE_CONTROL = 0xC0;
        //网关
        public final static int DEVICE_MESH_GATEWAY = 0XC1;
        //
    }

    /*
    * 遥控器
    * */
    //手持遥控器
    public static final int HAND_CONTROL = 0xC000;

    /*
    * 开关
    * */
    //面板开关
    public static final int SWITCH_PANEL = 0xC001;

    /*
    * 钥匙扣开关
    * */
    public static final int KEY_SWITCH = 0xC002;

    /*
  * 闹钟的类型
  */
    //场景闹钟
    public static final byte SCENE_ALARM = (byte) 0x92;
    //关闭闹钟
    public static final byte OFF_ALARM = (byte) 0x90;
    //开启闹钟
    public static final byte ON_ALARM = (byte) 0x91;

    /*
    *传感器 
    * */
    //风雨传感器
    public static final int SENSOR_WIND_AND_RAIN = 0xB000;
    //煤气传感器
    public static final int SENSOR_GAS = 0xB001;
    //门磁传感器
    public static final int SENSOR_DOOR_MAGNETIC = 0xB002;
    //人体红外线传感器
    public static final int SENSOR_INFRARED_BODY = 0xB003;
    //水浸传感器
    public static final int SENSOR_WATER_OUT = 0xB004;
    //温度传感器
    public static final int SENSOR_TEMPERATURE = 0xB005;
    //烟雾传感器
    public static final int SENSOR_SMOKE = 0xB006;
    //照度传感器
    public static final int SENSOR_ILLUMINATION = 0xB007;

    public static final String NAME = "name";

    //昼节律的ID
    public static final int SUNSETID = (byte) (15 & 0xFF);
    //夜节律
    public static final int SUNRISEID = (byte) (16 & 0xFF);


    public static final int SET_LANGUAGESET = 0;

    public static String ADD_NEW_ALARM = "new_alarm";
}
