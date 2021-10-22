module music.ui.db {
    requires sqlite.jdbc;
    requires java.sql;
    requires transitive music.ui.common;

    exports music.ui.db.datamodel;
}