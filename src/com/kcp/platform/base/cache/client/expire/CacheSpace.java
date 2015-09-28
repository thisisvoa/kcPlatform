package com.kcp.platform.base.cache.client.expire;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CacheSpace代表一个缓存空间，通过Ant风格的表达式指定缓存路径，所有这些缓存路径组成缓存空间，请看下面的例子.
 * <p>注：请参考<a href="http://ant.apache.org">Apache Ant</a>了解Ant风格的字符串的详细内容。
 *
 * <p>Ant风格的路径匹配规则:<br>
 *   <ul>
 *     <li>? 匹配一个字符</li>
 *     <li>* 匹配0个或多个字符</li>
 *     <li>** 匹配0个或多个路径中的“目录”</li>
 *  </ul>
 *
 * <p>一些例子:<br>
 *     <ul>
 *         <li><code>page/com/?/home</code> - 匹配 <code>page/com/1/home</code>以及<code>page/com/2/home</code> 或<code>page/com/a/home</code></li>
 *         <li><code>page/com/&#42;/home</code> - 匹配<code>page/com/abc/home</code>或<code>page/com//home</code>等。
 *         <li><code>page/com/&#42;&#42;</code> - 匹配所有前缀为page/com/的缓存路径，如<code>page/com/abc</code>或<code>page/com/abc/bce</code>等
 *         <li><code>page/com/&#42;&#42;/&#42;.html</code>
 *         - 匹配所有以 <code>.html</code>为后缀，以<code>page/com/</code>为前缀的缓存路径</li>
 *
 */
public class CacheSpace implements Serializable {
	private static final long serialVersionUID = -1691137067179291798L;
	private static final String SPACE_SPLITOR = "\\s*;\\s*";
    private static final String SPACE_NAME_PATTERN_SPLITOR = "\\s*:\\s*";

    //空间区域数量上限的默认值
    private static final int DEFAULT_SCOPE_AMOUNT = 100;

    //系统允许的最大空间区域数量上限值
    private static final int SYSTEM_MAX_SCOPE_AMOUNT = 1000;

     //系统允许的最小空间区域数量上限值
    private static final int SYSTEM_MIN_SCOPE_AMOUNT = 10;

    private String spaceName;
    private String cacheKeyPattern;
    private int maxScopeAmount = DEFAULT_SCOPE_AMOUNT;
    private PathMatcher cacheKeyMatcher;

    /**
     *
     * @param spaceName   缓存空间名，必须唯一
     * @param cacheKeyPattern   缓存键匹配模式字符串
     */
    public CacheSpace(String spaceName, String cacheKeyPattern) {
        this.spaceName = spaceName;
        this.cacheKeyPattern = cacheKeyPattern;
        this.cacheKeyMatcher = new AntPathMatcher();
    }

    public int getMaxScopeAmount() {
        return maxScopeAmount;
    }

    public void setMaxScopeAmount(int maxScopeAmount) {
        if(maxScopeAmount > SYSTEM_MAX_SCOPE_AMOUNT){
            this.maxScopeAmount = SYSTEM_MAX_SCOPE_AMOUNT;
        }else if(maxScopeAmount < SYSTEM_MIN_SCOPE_AMOUNT){
            this.maxScopeAmount = SYSTEM_MIN_SCOPE_AMOUNT;
        }else{
            this.maxScopeAmount = maxScopeAmount;
        }
    }

    public String getSpaceName() {
        return spaceName;
    }

    /**
     * 该缓存空间是否包含cacheKey的缓存键
     * @param cacheKey  缓存键
     * @return
     */
    public boolean containCache(String cacheKey) {
        return cacheKeyMatcher.match(cacheKeyPattern, cacheKey);
    }

    /**
     * 将缓存空间格式化字符串解析为CacheSpace集合，缓存空间格式化字符串定义如下：</br>
     * <pre>
     *     <空间名1>:<缓存路径模式串1>;<空间名1>:<缓存路径模式串2>
     * </pre>
     * @param cacheSpacesExpr
     * @return
     */
    public static List<CacheSpace> parseCacheSpaces(String cacheSpacesExpr) {
        if (!StringUtils.hasLength(cacheSpacesExpr)) {
            return Collections.emptyList();
        }
        String[] cacheSpaceItems = cacheSpacesExpr.split(SPACE_SPLITOR);
        List<CacheSpace> result = new ArrayList<CacheSpace>(cacheSpaceItems.length);
        for (String token : cacheSpaceItems) {
            result.add(parseCacheSpace(token));
        }
        return result;
    }

    private static CacheSpace parseCacheSpace(String cacheSpaceExpr) {
        Assert.hasLength(cacheSpaceExpr, "'CommandGroupStr'必须有值");
        String[] items = cacheSpaceExpr.split(SPACE_NAME_PATTERN_SPLITOR);
        Assert.isTrue(items.length >=2 && items.length <=3 , "格式不合法，必须是 <分组名>,<匹配串>[,<究竟最大命令数据>]");
        CacheSpace cacheSpace = new CacheSpace(items[0], items[1]);
        if(items.length == 3){
            try {
                cacheSpace.setMaxScopeAmount(Integer.parseInt(items[2]));
            } catch (NumberFormatException e) {
                cacheSpace.setMaxScopeAmount(CacheSpace.DEFAULT_SCOPE_AMOUNT);
            }
        }
        return cacheSpace;
    }


    public static CacheSpace valueOf(String cacheSpaceExpr) {
        return parseCacheSpace(cacheSpaceExpr);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.getSpaceName())
                .hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        if (obj instanceof CacheSpace) {
            CacheSpace other = (CacheSpace) obj;
            equals = new EqualsBuilder()
                    .append(this.spaceName, other.getSpaceName())
                    .isEquals();
        }
        return equals;
    }
}
