package me.ampayne2.UltimateGames.Database;

import com.alta189.simplesave.Database;
import com.alta189.simplesave.DatabaseFactory;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;
import com.alta189.simplesave.mysql.MySQLConfiguration;
import com.alta189.simplesave.sqlite.SQLiteConfiguration;

import me.ampayne2.UltimateGames.Database.Tables.PlayerData;
import me.ampayne2.UltimateGames.UltimateGames;

public class DatabaseManager {

	private final UltimateGames plugin;
	private final Database db;
	public DatabaseManager(UltimateGames plugin) throws ConnectionException, TableRegistrationException {
		this.plugin = plugin;

		String dbType = plugin.getConfig().getString("Database.Type");

		if (dbType.equalsIgnoreCase("sqlite")) {
			SQLiteConfiguration sqLiteConfiguration = new SQLiteConfiguration();
			sqLiteConfiguration.setPath(plugin.getDataFolder() + "database.db");
			sqLiteConfiguration.setPrefix(plugin.getConfig().getString("Database.Prefix"));
			db = DatabaseFactory.createNewDatabase(sqLiteConfiguration);
		} else if (dbType.equalsIgnoreCase("mysql")) {
			MySQLConfiguration mySQLConfiguration = new MySQLConfiguration();
			mySQLConfiguration.setHost(plugin.getConfig().getString("Database.Address"));
			mySQLConfiguration.setPort(plugin.getConfig().getInt("Database.Port"));
			mySQLConfiguration.setUser(plugin.getConfig().getString("Database.User"));
			mySQLConfiguration.setPassword(plugin.getConfig().getString("Database.Password"));
			mySQLConfiguration.setDatabase(plugin.getConfig().getString("Database.Database"));
			mySQLConfiguration.setPrefix(plugin.getConfig().getString("Database.Prefix"));
			db = DatabaseFactory.createNewDatabase(mySQLConfiguration);
		} else {
			throw new ConnectionException("Invalid database configuration!");
		}

		db.registerTable(PlayerData.class);
		db.connect();
	}

	public Database getDatabase() {
		return db;
	}
}
