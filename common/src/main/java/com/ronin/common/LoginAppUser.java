package com.ronin.common;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lizelong
 * @date Created on 2020/7/6 11:40
 * @description
 */
@Data
public class LoginAppUser extends AppUser implements UserDetails {
    private static final long serialVersionUID = 1104483135524625763L;

    /**
     * 归属公司
     */
    private Long ascription;

    /**
     * 用户id
     */
    private Long personId;

    /**
     * 人员头像字段
     **/
    private String avatar;

    /**
     * 平台超管
     */
    private Boolean superAdmin;

    /**
     * 组织管理员
     */
    private Boolean orgAdmin;

    /**
     * 0.超管 1.内部员工 2.外部用户
     */
    private Integer attributeType;

    /**
     * 系统应用的Code
     */
    private Set<String> applications;
    /**
     * 系统应用的ID
     */
    private Set<Long> appIds;

    /**
     * 权限控制的Code
     */
    private Set<String> permissions;

    /**
     * 应用角色的ID
     */
    private Set<Long> sysRoles;

    /**
     * 归属组织架构 -- 事业部
     */
    private Set<BaseDTO> deptOrg;

    /**
     * 归属组织架构 -- 营业店
     */
    private Set<BaseDTO> storeOrg;

    /**
     * 数据权限集合 -- 事业部
     */
    private Set<Long> deptScope;

    /**
     * 数据权限集合 -- 营业店
     */
    private Set<Long> storeScope;

    /**
     * 是否允许加班 0--否  1是
     **/
    private Integer allowOvertime;

    /**
     * 是否允许场外作业
     **/
    private Integer allowSeconded;

    /**
     * 人员职位
     **/
    private String position;
    /**
     * 公司手机号
     */
    private String companyPhone;

    private String loginType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new HashSet<>();
        // TODO 角色是否需要
//		    if (!CollectionUtils.isEmpty(sysRoles)) {
//			      sysRoles.forEach(role -> {
//                if (StringUtils.startsWithIgnoreCase(role, "ROLE_")) {
//                    collection.add(new SimpleGrantedAuthority(role));
//                } else {
//                    collection.add(new SimpleGrantedAuthority("ROLE_" + role));
//                }
//			      });
//		    }
        if (!CollectionUtils.isEmpty(permissions)) {
            permissions.forEach(per -> collection.add(new SimpleGrantedAuthority(per)));
        }
        return collection;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
