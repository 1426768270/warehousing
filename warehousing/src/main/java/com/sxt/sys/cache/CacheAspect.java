package com.sxt.sys.cache;

import com.sxt.sys.domain.Dept;
import com.sxt.sys.domain.User;
import com.sxt.sys.vo.DeptVo;
import com.sxt.sys.vo.UserVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@EnableAspectJAutoProxy
public class CacheAspect {

    /**
     * 日志处理
     */
    private Log log = LogFactory.getLog(CacheAspect.class);

    //声明一个缓存容器
    private Map<String,Object> CACHE_CONTAINER = new HashMap<>();

    public Map<String, Object> getCACHE_CONTAINER() {
        return CACHE_CONTAINER;
    }
    //声明切面表达式
    private static final String POINTCUT_DEPT_UPDATE = "execution(* com.sxt.sys.service.impl.DeptServiceImpl.updateById(..))";
    private static final String POINTCUT_DEPT_ADD = "execution(* com.sxt.sys.service.impl.DeptServiceImpl.save(..))";
    private static final String POINTCUT_DEPT_GET = "execution(* com.sxt.sys.service.impl.DeptServiceImpl.getById(..))";
    private static final String POINTCUT_DEPT_DELETE = "execution(* com.sxt.sys.service.impl.DeptServiceImpl.delete(..))";

    //前缀
    private static final String CACHE_DEPT_PROFIX="dept:";

    /**
     * 查询切入
     */
    @Around(value = POINTCUT_DEPT_GET)
    public Object cacheDeptGet(ProceedingJoinPoint joinPoint) throws Throwable {

        //取出第一个参数
        Integer object = (Integer) joinPoint.getArgs()[0];

        //从缓存中取
        Object res1 = CACHE_CONTAINER.get(CACHE_DEPT_PROFIX+object);
        if(res1!=null) {
            log.info("从缓存里面找到部门对象"+CACHE_DEPT_PROFIX+object);
            return res1;
        }else {
            Dept res2 = (Dept) joinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_DEPT_PROFIX+res2.getId(), res2);
            log.info("未从缓存里面找到部门对象，从数据库查询"+CACHE_DEPT_PROFIX+res2.getId());
            return res2;
        }

    }

    /**
     * 添加切入
     */
    @Around(value = POINTCUT_DEPT_ADD)
    public Object cacheDeptAdd(ProceedingJoinPoint joinPoint) throws Throwable {

        //取出第一个参数
        Dept object = (Dept) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();

        if(res) {
            log.info("保存对象"+CACHE_DEPT_PROFIX+object);
            CACHE_CONTAINER.put(CACHE_DEPT_PROFIX+object.getId(), object);
        }
        return res;
    }
    /**
     * 更新切入
     */
    @Around(value = POINTCUT_DEPT_UPDATE)
    public Object cacheDeptUpdate(ProceedingJoinPoint joinPoint) throws Throwable {

        //取出第一个参数
        DeptVo deptVo = (DeptVo) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();

        if (isSuccess){
            Dept dept= new Dept();
            BeanUtils.copyProperties(deptVo,dept);
            log.info("部门对象缓存已更新"+CACHE_DEPT_PROFIX+deptVo.getId());
            CACHE_CONTAINER.put(CACHE_DEPT_PROFIX + deptVo.getId(),dept);
        }
        return isSuccess;
    }


    /**
     * 删除切入
     */
    @Around(value = POINTCUT_DEPT_DELETE)
    public Object cacheDeptDelete(ProceedingJoinPoint joinPoint) throws Throwable {

        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();

        if (isSuccess){
            CACHE_CONTAINER.remove(CACHE_DEPT_PROFIX+id);
            log.info("部门对象缓存已删除"+CACHE_DEPT_PROFIX+id);
        }
        return isSuccess;
    }

    private static final String POINTCUT_USER_UPDATE = "execution(* com.sxt.sys.service.impl.UserServiceImpl.updateById(..))";
    private static final String POINTCUT_USER_ADD = "execution(* com.sxt.sys.service.impl.UserServiceImpl.save(..))";
    private static final String POINTCUT_USER_GET = "execution(* com.sxt.sys.service.impl.UserServiceImpl.getById(..))";
    private static final String POINTCUT_USER_DELETE = "execution(* com.sxt.sys.service.impl.UserServiceImpl.delete(..))";
    //前缀
    private static final String CACHE_USER_PROFIX="user:";

    /**
     * 查询切入
     */
    @Around(value = POINTCUT_USER_GET)
    public Object cacheUserGet(ProceedingJoinPoint joinPoint) throws Throwable {
        // 取出第一个参数
        Integer object = (Integer) joinPoint.getArgs()[0];
        // 从缓存里面取
        Object res1 = CACHE_CONTAINER.get(CACHE_USER_PROFIX + object);
        if (res1 != null) {
            log.info("已从缓存里面找到用户对象" + CACHE_USER_PROFIX + object);
            return res1;
        } else {
            User res2 = (User) joinPoint.proceed();

            CACHE_CONTAINER.put(CACHE_USER_PROFIX + res2.getId(), res2);
            log.info("未从缓存里面找到用户对象，去数据库查询并放到缓存"+CACHE_USER_PROFIX+res2.getId());
            return res2;
        }
    }
    /**
     * 添加切入
     */
    @Around(value = POINTCUT_USER_ADD)
    public Object cacheUserAdd(ProceedingJoinPoint joinPoint) throws Throwable {

        //取出第一个参数
        User object = (User) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();

        if(res) {
            log.info("保存对象"+CACHE_USER_PROFIX+object);
            CACHE_CONTAINER.put(CACHE_USER_PROFIX+object.getId(), object);
        }
        return res;
    }
    /**
     * 更新切入
     */
    @Around(value = POINTCUT_USER_UPDATE)
    public Object cacheUserUpdate(ProceedingJoinPoint joinPoint) throws Throwable {

        //取出第一个参数
        UserVo userVo = (UserVo) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();

        if (isSuccess){
            User user= new User();
            BeanUtils.copyProperties(userVo,user);
            log.info("部门对象缓存已更新"+CACHE_USER_PROFIX+userVo.getId());
            CACHE_CONTAINER.put(CACHE_USER_PROFIX + userVo.getId(),user);
        }
        return isSuccess;
    }


    /**
     * 删除切入
     */
    @Around(value = POINTCUT_USER_DELETE)
    public Object cacheUserDelete(ProceedingJoinPoint joinPoint) throws Throwable {

        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();

        if (isSuccess){
            CACHE_CONTAINER.remove(CACHE_USER_PROFIX+id);
            log.info("用户对象缓存已删除"+CACHE_USER_PROFIX+id);
        }
        return isSuccess;
    }
}
