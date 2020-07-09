package com.ronin.common;

/**
 * <p>Description: [系统常量]</p>
 * Created on 2019年08月16日
 * @author <a href="mailto: hexiaobo@camelotchina.com">贺小波</a>
 * @version 1.0
 * Copyright (c) 2019 北京柯莱特科技有限公司
 */
public final class SysConstant {

    /**商机编码前缀*/
    public static final String CHANCENO_PREFIX = "SJ";

    /**项目编码前缀*/
    public static final String PROJECTNO_PREFIX = "XM";

    /**商机投标前缀*/
    public static final String CHANCENO_BID_PREFIX = "YWTB";

    /**商机报价前缀*/
    public static final String CHANCENO_OFFER_PREFIX = "LXBJ";

    /**商机转交易前缀*/
    public static final String CHANCENO_DEAL_PREFIX = "ZJY";
    
    /**线索编码前缀*/
    public static final String CLUENO_PREFIX = "XS";
    
    /**查询权限编码前缀*/
    public static final String QUERY_PERMISSION_PREFIX = "QM";

    /**潜在客户编码前缀*/
    public static final String CUSTOMER_PREFIX = "QZKH";

    /**客户联系人编码前缀*/
    public static final String CUSTOMER_CONTACT_PREFIX = "KHLXR";

    /**系统人员ID*/
    public static final Long SYS_USER_ID = 0L;

    /**系统操作*/
    public static final String SYS_USER_NAME = "系统操作";

    /**数值为-1L*/
    public static final Long NEGATIVE_ONE = -1L;

    /**数值为-1*/
    public static final int MINUS_ONE = -1;

    /**数值为0*/
    public static final int ZERO = 0;

    /**数值为1*/
    public static final int ONE = 1;

    /**数值为2*/
    public static final int TWO = 2;

    /**数值为3*/
    public static final int THREE = 3;

    /**数值为4*/
    public static final int FOUR = 4;

    /**数值为5*/
    public static final int FIVE = 5;

    /**数值为6*/
    public static final int SIX = 6;

    /**数值为7*/
    public static final int SEVEN = 7;

    /**数值为8*/
    public static final int EIGHT = 8;

    /**数值为9*/
    public static final int NINE = 9;

    /**数值为10*/
    public static final int TEN = 10;

    /**数值为30*/
    public static final int THIRTY = 30;

    /**数值为60*/
    public static final int SIXTY = 60;
    
    /**数值为15*/
    public static final int FIFTEEN = 15;

    /**数值为1000*/
    public static final int ONE_THOUSAND = 1000;

    /**数值为2000*/
    public static final int TWO_THOUSAND = 2000;

    /**数值为1500*/
    public static final int ONE_THOUSAND_FIVE_HUNDRED = 1500;

    /**数值为60000*/
    public static final int SIXTY_THOUSAND = 60000;

    /**字节长度1024*/
    public static final int BYTE_LENGTH = 1024;

    /**字节纠正256*/
    public static final int BYTE_CORRECT = 256;

    /**数值为201*/
    public static final int TWO_HUNDRED_AND_ONE = 201;

    /**数值为200*/
    public static final int TWO_HUNDRED = 200;

    /**系统编码redis缓存key*/
    public static final String CODEYMDREDISKEY = "SYS_CODE_YMD_REDIS_KEY";

    /**审核通过*/
    public static final String REFUSE = "审核通过";
    
    /**该地址不在考勤打卡区域，请移至考勤区域打卡。*/
    public static final String CHECKIN_ILLEGAL_CARDING_AREA = "该地址不在考勤打卡区域，请移至考勤区域打卡。";
    
    /**时间标识异常，请确认*/
    public static final String CHECKIN_ILLEGAL_CARDING_TIME = "时间标识异常，请确认";
    
    /**上班考勤*/
    public static final String CHECKIN_MORING = "上班考勤";
    
    /**中午考勤*/
    public static final String CHECKIN_NOON = "中午考勤";
    
    /**下班考勤*/
    public static final String CHECKIN_AFTERNOON = "下班考勤";
    
    /**{}次待确认*/
    public static final String CHECKIN_UNCONFIRMEDCOUNT = "次待确认";
    
    /**对不起，暂无下一级人员记录*/
    public static final String CHECKIN_EMPTY_PERSONNEL_RECORDS = "对不起，您名下暂无下一级人员信息";
    
    /**考勤异常*/
    public static final String CHECKIN_ABNORMAL_ATTENDANCE = "考勤异常";
    
    /**次*/
    public static final String FREQUENCY = "次";
    
    /**你手机时间与本地标准时间相差较大*/
    public static final String CHECKIN_DIFF_TIME = "你手机时间与本地标准时间相差较大";
    
    /**异常考勤*/
    public static final String ABNORMAL_ATTENDANCE= "异常考勤";
    
    /**我的*/
    public static final String MY = "我的";
    
    /**下属*/
    public static final String BRANCH = "下属";
    
    /**考勤*/
    public static final String ATTENDANCE = "考勤";
    
    /**待确认*/
    public static final String UNCONFIRMED = "待确认";
    
    /**之前*/
    public static final String BEFORE = "之前";
    
    /**之后*/
    public static final String AFTER = "之后";
    
    /**个*/
    public static final String INDIVIDUAL = "个";
    
    /**缺失*/
    public static final String DEFECT = "缺失";
    
    /**暂未开放打卡*/
    public static final String CARDING_IS_NOT_OPEN_YET = "暂未开放打卡";
    
    /** 暂未打卡，暂无考勤地点详情*/
    public static final String NO_CARD_AND_ATTENDANCE_DETAILS = "暂未打卡，暂无考勤地点详情";
	/**
	 * 超级管理员
	 */
	public static final String SUPER_ADMIN = "superAdmin";
	/**
	 * 工作流用户
	 */
	public static final String WORKFLOW_USER = "workflowUser";
	/**默认分页pageSize*/
    public static final Integer PAGE_SIZE=20;
    
    public static final String Y="Y";
    
    public static final String N="N";
    /**常量类禁止new创建*/
    private SysConstant() {
        super();
    }

}
