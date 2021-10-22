package music.ui.db.datamodel;

import music.ui.common.Album;
import music.ui.common.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static music.ui.db.datamodel.Constants.*;


public class Datasource {

    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\use4r-pc\\Desktop\\Java\\Music\\" + DB_NAME;


    private PreparedStatement querySongInfoView;
    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;
    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;
    private PreparedStatement querySongs;
    private PreparedStatement queryAlbumsByArtistID;
    private PreparedStatement updateArtistName;


    private Connection conn;

    private static Datasource instance = new Datasource();
    private Datasource() {

    }

    public static Datasource getInstance() {
        return instance;
    }




    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            querySongInfoView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            insertIntoArtists = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = conn.prepareStatement(INSERT_SONGS);
            queryArtist = conn.prepareStatement(QUERY_ARTIST);
            queryAlbum = conn.prepareStatement(QUERY_ALBUM);
            querySongs = conn.prepareStatement(QUERY_SONGS);
            queryAlbumsByArtistID = conn.prepareStatement(QUERY_ALBUMS_BY_ARTIST_ID);
            updateArtistName = conn.prepareStatement(UPDATE_ARTIST_NAME);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to the database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (querySongInfoView != null) {
                querySongInfoView.close();
            }
            if (insertIntoArtists != null) {
                insertIntoArtists.close();
            }

            if (insertIntoAlbums != null) {
                insertIntoAlbums.close();
            }

            if (insertIntoSongs != null) {
                insertIntoSongs.close();
            }

            if (queryArtist != null) {
                queryArtist.close();
            }

            if (queryAlbum != null) {
                queryAlbum.close();
            }

            if(querySongs != null) {
                querySongs.close();
            }

            if(queryAlbumsByArtistID != null) {
                queryAlbumsByArtistID.close();
            }

            if(updateArtistName != null) {
                updateArtistName.close();
            }


            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public List<Artist> queryArtists(int sortOrder) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);

        appendSortOrder(sb, QUERY_ARTISTS_SORT, sortOrder);

//        if (sortOrder != ORDER_BY_NONE) {
//            sb.append(QUERY_ARTISTS_SORT);
//            if (sortOrder == ORDER_BY_DESC) {
//                sb.append("DESC");
//            } else {
//                sb.append("ASC");
//            }
//        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Artist> artists = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                }catch (InterruptedException e) {
                    System.out.println("Interrupted : " + e.getMessage());
                }
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));
                artist.setName(results.getString(INDEX_ARTIST_NAME));

                artists.add(artist);
            }

            return artists;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
//        finally {
//            try {
//                if(results != null) {
//                    results.close();
//                }
//            }catch (SQLException e) {
//                // Oh Dear
//                System.out.println("Error closing ResultSet: " + e.getMessage());
//            }
//            try {
//                if(statement != null) {
//                    statement.close();
//                }
//            }catch (SQLException e) {
//                // Oh well
//                System.out.println("Error closing Statement: " + e.getMessage());
//            }
//        }
    }


    public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {

        // SELECT albums.name FROM albums INNER JOIN artists ON albums.artist = artists._id WHERE artists.name = "Carole King" ORDER BY albums.name COLLATE NOCASE ASC;
        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(artistName);
        sb.append("\"");


        appendSortOrder(sb, QUERY_ALBUMS_BY_ARTIST_SORT, sortOrder);
//        if (sortOrder != ORDER_BY_NONE) {
//            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
//            if (sortOrder == ORDER_BY_DESC) {
//                sb.append("DESC");
//            } else {
//                sb.append("ASC");
//            }
//        }


        sb.append(";");
        System.out.println("SQL Statement = " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<String> albums = new ArrayList<>();
            while (results.next()) {
                albums.add(results.getString(1));
            }
            return albums;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }


    }



    private StringBuilder appendSortOrder(StringBuilder sb, String adjoiningString, int sortOrder) {
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(adjoiningString);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }
        return sb;
    }


    public void querySongsMetaData() {
        String sql = "SELECT * FROM " + TABLE_SONGS;

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            ResultSetMetaData meta = results.getMetaData();
            int numColumns = meta.getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                System.out.format("Column %d in the songs table is named %s\n", i, meta.getColumnName(i));
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }

    public int getCount(String table) {
        String sql = "SELECT COUNT(*) AS count, MIN(_id) AS min_id FROM " + table;

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            int count = results.getInt("count");
            int min = results.getInt("min_id");

            System.out.format("Count = %d, Min = %d\n", count, min);
            return count;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }


    public boolean createViewForSongArtists() {
        try (Statement statement = conn.createStatement()) {

            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;
        } catch (SQLException e) {
            System.out.println("Create View failed: " + e.getMessage());
            return false;
        }
    }


    private int insertArtist(String name) throws SQLException {
        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            // Insert the artist, as they are not on file
            insertIntoArtists.setString(1, name);
            int affectedRows = insertIntoArtists.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert artist");
            }

            ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for artist");
            }
        }
    }

    private int insertAlbum(String name, int artistID) throws SQLException {
        queryAlbum.setString(1, name);
        ResultSet results = queryAlbum.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            // Insert the artist, as they are not on file
            insertIntoAlbums.setString(1, name);
            insertIntoAlbums.setInt(2, artistID);
            int affectedRows = insertIntoAlbums.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert album!");
            }

            ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for album!");
            }
        }
    }

    public boolean updateArtistName(int id, String newName) {
        try {
            updateArtistName.setString(1, newName);
            updateArtistName.setInt(2, id);
            int affectedRecords = updateArtistName.executeUpdate();

            return affectedRecords == 1;
        }catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }


    public void insertSong(String title, String artist, String album, int track) {

        try {
            conn.setAutoCommit(false);
            querySongs.setString(1, title);

            ResultSet results = querySongs.executeQuery();
            if(results.next()) {
                System.out.println("Song already exists");
            }else {
                int artistID = insertArtist(artist);
                int albumID = insertAlbum(album, artistID);
                insertIntoSongs.setInt(1, track);
                insertIntoSongs.setString(2, title);
                insertIntoSongs.setInt(3, albumID);
                int affectedRows = insertIntoSongs.executeUpdate();
                if (affectedRows == 1) {
                    conn.commit();
                } else {
                    throw new SQLException("The song insert failed");
                }
            }


        } catch (Exception e) {
            System.out.println("Insert song exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Rollback exception: " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behaviour");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit: " + e.getMessage());
            }
        }
    }

    public List<Album> queryAlbumsForArtistID(int id) {
        try {
            queryAlbumsByArtistID.setInt(1, id);
            ResultSet results = queryAlbumsByArtistID.executeQuery();

            List<Album> albums = new ArrayList<>();
            while(results.next()) {
                Album album = new Album();
                album.setId(results.getInt(1));
                album.setName(results.getString(2));
                album.setArtistID(id);

                albums.add(album);
            }

            return albums;
        }catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }


}
