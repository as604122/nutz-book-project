package net.wendal.nutzbook.yvr;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import net.sf.ehcache.CacheManager;
import net.wendal.nutzbook.common.util.Markdowns;
import net.wendal.nutzbook.core.service.AuthorityService;
import net.wendal.nutzbook.yvr.service.YvrService;

public class YvrMainSetup implements Setup {
    
    private static final Log log = Logs.get();

    @Override
    public void init(NutConfig nc) {
        Ioc ioc = nc.getIoc();
        Daos.createTablesInPackage(nc.getIoc().get(Dao.class), getClass().getPackage().getName() + ".bean", false);
        // 检查一下Ehcache CacheManager 是否正常.
        CacheManager cacheManager = ioc.get(CacheManager.class);
        log.debug("Ehcache CacheManager = " + cacheManager);
        // CachedNutDaoExecutor.DEBUG = true;

        // 设置Markdown缓存
        if (cacheManager.getCache("markdown") == null)
            cacheManager.addCache("markdown");
        Markdowns.cache = cacheManager.getCache("markdown");

        ioc.get(YvrService.class).updateTopicTypeCount();
        ioc.get(AuthorityService.class).initFormPackage(getClass().getPackage().getName());
    }

    @Override
    public void destroy(NutConfig nc) {
        Markdowns.cache = null;
    }

}
