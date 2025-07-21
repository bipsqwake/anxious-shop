package com.bipsqwake.anxios_shop_api.adminbot;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.abilitybots.api.db.MapDBContext;

@Configuration
public class DBContextCreator {

    @Bean
    public DBContext customPathOnlineContext(@Value("${appconfig.bot.data.dir}") String dbFile) {
        DB db = DBMaker
                .fileDB(dbFile)
                .fileMmapEnableIfSupported()
                .closeOnJvmShutdown()
                .transactionEnable()
                .make();

        return new MapDBContext(db);
    }

}
