/**
 * 通用缓存模块
 * <p>
 * 提供统一的缓存管理功能，简化各业务模块的缓存实现，提高代码复用性和一致性。
 * <p>
 * 主要功能：
 * <ul>
 *     <li>统一的缓存键生成策略</li>
 *     <li>缓存的获取、设置、删除操作</li>
 *     <li>基于注解的声明式缓存</li>
 *     <li>支持SpEL表达式的缓存键和条件</li>
 *     <li>支持模式匹配的批量缓存清除</li>
 *     <li>多租户、多域的缓存隔离</li>
 * </ul>
 *
 * @author musk-functions-cache
 */
package org.example.musk.functions.cache;
