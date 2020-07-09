package com.ronin.common;
/**
 * <p>Description: [Redis常量]</p>
 * Created on 2019年9月12日
 * @author wuxiaoxian 
 * @version 1.0
 */
public class SysRedisConstant {

	 /**缓存失效时间**/
    public static final Long  Cache_Invalidation = (long) 43200;
    
    public static final String SYS_ACCOUNT = "sys_account";
    
    public static final String SYS_ACC_ORG = "sys_acc_org";
    
    public static final String SYS_ORG = "sys_org";
    
    public static final String SYS_ACC_OPERATE = "sys_acc_operate";
	
    public static final String SYS_OPERATE = "sys_operate";

    public static final String SYS_PERSON_INFO = "sys_person_info";
    
    public static final String SYS_PERSON_INFO_EXTEND = "sys_person_info_extend";
    
    public static final String SYS_ORG_SUB_WF_USERS = "sys_org_sub_wf_users";
    
    public static final String SYS_ORG_WF_LEADER = "sys_org_sub_wf_leader";
    
    public static final String SYS_WF_ORG_MSG = "sys_wf_org_msg";

    public static final String SYS_SHOP_ORG_MANAGER = "sys_shop_org_manager";
    
    public static final String SYS_ORG_ORGLEADER = "sys_org_orgLeader";

    /**百度云redis缓存key*/
    public static final String SYS_BAIDUYUN_ACCESS_TOKEN_KEY = "sys_baiduyun_access_token_key";
    
    /*********************WPS***************************/
    public static final String WPS_WORK_PLAN_COMMON = "wps_work_plan_common";

	/**登录用户的数据块Key**/
	public static final String SAAS_USERDATABLOCK_ACCID = "saas:userdatablock:accid:";

	/**登录用户角色拥有的数据块Key**/
	public static final String SAAS__ROLEDATABLOCK_ACCID = "saas:roledatablock:accid:";

    public static final String SAAS_APP_ACCID = "saas:app:accid:";
	/** 数据块详情*/
    public static final String SAAS_INFODATABLOCK_ID = "saas:infodatablock:id:";
    /** 网点详情*/
    public static final String SAAS_NETWORK_CODE = "saas:network:code:";
    /** 账户详情*/
    public static final String SAAS_ACCOUNT_CODE = "saas:account:code:";
    /** 账户密码*/
    public static final String SAAS_ACCOUNT_PWD = "saas:account:pwd:";
    /** 子地区列表 **/
    public static final String SAAS_NETWORK_CHILD_DISTRICT = "saas:network:childDistrict:";
    /** 区域列表 **/
    public static final String SAAS_NETWORK_ALL_DISTRICT = "saas:network:allDistrict";
    /** 地区 **/
    public static final String SAAS_NETWORK_DISTRICT = "saas:network:district:";
    /** 数据权限 **/
    public static final String SAAS_DATAPERMISSOIN = "saas:datapermission:";
    /** 字典**/
    public static final String SAAA_DICT = "saas:dict:dicType";
    /** 全量地区数据map **/
    public static final String SAAS_All_DISTRICT_MAP = "saas.allDistrict";
    /** 职级信息 **/
    public static final String SAAS_HCM_JOB_CLASS = "saas:orgJobClass:";
    /** 岗位序列信息 **/
    public static final String SAAS_HCM_JOB_CATEGORY_ALL = "saas.jobCategory";
    /** 职位信息 **/
    public static final String SAAS_POSITION = "saas:orgPositon:";
	/**
	 * 一天的秒数
	 */
	public static final Long ONEDAY = 24 * 60 * 60L;
    /*********************战报接口***************************/
    /**
     * 拜访客户数量接口 12*60*60
     */
    public static final String ZBBFCLENTCACHE = "ZBBFCLENTCACHE|";

    /**
     * 签约客户数/台数接口 12*60*60
     */
    public static final String ZBQYCLENTCACHE = "ZBQYCLENTCACHE|";

    /**
     * 进场台数接口 12*60*60
     */
    public static final String ZBJCPCSCACHE = "ZBJCPCSCACHE|";

    /**
     * 结算金额接口 12*60*60
     */
    public static final String ZBJSCACHE = "ZBJSCACHE|";

    /**
     * 收款金额接口 12*60*60
     */
    public static final String ZBQYCACHE = "ZBQYCACHE|";

    /**
     * 退场台数接口 12*60*60
     */
    public static final String ZBTCAPCSCACHE = "ZBTCAPCSCACHE|";

    /**
     * 在租台数接口 12*60*60
     */
    public static final String ZBZZPCSCACHE = "ZBZZPCSCACHE|";

    /**
     * 战报人员数据 12*60*60
     */
    public static final String ZBSTAFFCACHE = "ZBSTAFFCACHE|";

    /**
     * 战报人员权限数据 60*60
     */
    public static final String ZBROLECACHE = "ZBROLECACHE|";

    /**
     * 资产设备数据 12*60*60
     */
    public static final String ZBZCSBCACHE = "ZBZCSBCACHE|";

    /**常量类禁止new创建*/
    private SysRedisConstant() {
        super();
    }
}
