package music.ui.db.datamodel;

public class Constants {

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;


    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;


    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;


    public static final String QUERY_ALBUMS_BY_ARTIST_START =
            "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " FROM " + TABLE_ALBUMS +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ARTIST +
                    " = " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_ID +
                    " WHERE " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME + " = \"";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT =
            " ORDER BY " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    public static final String QUERY_ARTISTS_START =
            "SELECT * FROM " + TABLE_ARTISTS;

    public static final String QUERY_ARTISTS_SORT =
            " ORDER BY " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME + " COLLATE NOCASE ";

    public static final String QUERY_ARTISTS_FOR_SONG_START = "SELECT " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + '.' +
            COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + '.' + COLUMN_SONG_TRACK + " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + '.' + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ID +
            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_ID +
            " WHERE " + TABLE_SONGS + '.' + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_ARTISTS_FOR_SONG_SORT = " ORDER BY " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME + ", " +
            TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";
    public static final String TABLE_ARTIST_SONG_COLUMN_ARTIST = "artist";
    public static final String TABLE_ARTIST_SONG_COLUMN_ALBUM = "album";
    public static final String TABLE_ARTIST_SONG_COLUMN_TRACK = "track";
    public static final String TABLE_ARTIST_SONG_COLUMN_TITLE = "title";
    public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " + TABLE_ARTIST_SONG_VIEW + " AS SELECT " +
            TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME + " AS artist, " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " AS album, " + TABLE_SONGS + '.' + COLUMN_SONG_TRACK + " AS track, " +
            TABLE_SONGS + '.' + COLUMN_SONG_TITLE + " AS title FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + '.' +
            COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ID + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ARTIST +
            " = " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_ID + " ORDER BY " + TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + '.' +
            COLUMN_SONG_TRACK;

    public static final String QUERY_VIEW_SONG_INFO = "SELECT " + TABLE_ARTIST_SONG_COLUMN_ARTIST + ", " +
            TABLE_ARTIST_SONG_COLUMN_ALBUM + ", " + TABLE_ARTIST_SONG_COLUMN_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW + " WHERE " +
            TABLE_ARTIST_SONG_COLUMN_TITLE + " = \"";

    public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT " + TABLE_ARTIST_SONG_COLUMN_ARTIST + ", " +
            TABLE_ARTIST_SONG_COLUMN_ALBUM + ", " + TABLE_ARTIST_SONG_COLUMN_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + TABLE_ARTIST_SONG_COLUMN_TITLE + " = ?";

    public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS + "(" + COLUMN_ARTIST_NAME + ")" +
            " VALUES(?)";
    public static final String INSERT_ALBUMS = "INSERT INTO " + TABLE_ALBUMS + "(" + COLUMN_ALBUM_NAME + ", " +
            COLUMN_ALBUM_ARTIST + ")" + " VALUES(?, ?)";
    public static final String INSERT_SONGS = "INSERT INTO " + TABLE_SONGS + "(" + COLUMN_SONG_TRACK + ", " +
            COLUMN_SONG_TITLE + ", " + COLUMN_SONG_ALBUM + ") VALUES(?, ?, ?)";

    public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " +
            TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";

    public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " +
            TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";

    public static final String QUERY_SONGS = "SELECT " + COLUMN_SONG_ID + " FROM " + TABLE_SONGS + " WHERE " +
            COLUMN_SONG_TITLE + " = ?";

    public static final String QUERY_ALBUMS_BY_ARTIST_ID = "SELECT * FROM " + TABLE_ALBUMS +
            " WHERE " + COLUMN_ALBUM_ARTIST + " = ? ORDER BY " + COLUMN_ALBUM_NAME + " COLLATE NOCASE";

    public static final String UPDATE_ARTIST_NAME = "UPDATE " + TABLE_ARTISTS + " SET " + COLUMN_ARTIST_NAME +
            " = ? WHERE " + COLUMN_ARTIST_ID + " = ?";
}
