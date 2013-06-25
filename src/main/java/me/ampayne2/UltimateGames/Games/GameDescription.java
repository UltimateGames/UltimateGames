package me.ampayne2.UltimateGames.Games;

import me.ampayne2.UltimateGames.Enums.PlayerType;
import me.ampayne2.UltimateGames.Enums.ScoreType;

public class GameDescription {
	private String name, description, version, author, pack, scoreTypeName, secondaryScoreTypeName;
	private ScoreType scoreType, secondaryScoreType;
	private PlayerType playerType;

	/**
	 * Gets the name of the game
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description of the game
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the version string of the game
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the author of the game
	 * 
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Gets the main class of the game
	 * 
	 * @return the main class
	 */
	public String getPackage() {
		return pack;
	}

	/**
	 * Gets the main ScoreType of the game
	 * 
	 * @return
	 */
	public ScoreType getScoreType() {
		return scoreType;
	}

	/**
	 * Gets the secondary ScoreType of the game
	 * 
	 * @return
	 */
	public ScoreType getSecondaryScoreType() {
		return secondaryScoreType;
	}

	/**
	 * Gets the name of the main ScoreType of the game
	 * 
	 * @return
	 */
	public String getScoreTypeName() {
		return scoreTypeName;
	}

	/**
	 * Gets the name of the secondary ScoreType of the game
	 * 
	 * @return
	 */
	public String getSecondaryScoreTypeName() {
		return secondaryScoreTypeName;
	}

	/**
	 * Gets the PlayerType of the game
	 * 
	 * @return
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setPackage(String pack) {
		this.pack = pack;
	}

	protected void setVersion(String version) {
		this.version = version;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	protected void setAuthor(String author) {
		this.author = author;
	}

	protected void setScoreType(ScoreType scoreType) {
		this.scoreType = scoreType;
	}

	protected void setSecondaryScoreType(ScoreType secondaryScoreType) {
		this.secondaryScoreType = secondaryScoreType;
	}
	
	protected void setScoreTypeName(String scoreTypeName) {
		this.scoreTypeName = scoreTypeName;
	}
	
	protected void setSecondaryScoreTypeName(String secondaryScoreTypeName) {
		this.secondaryScoreTypeName = secondaryScoreTypeName;
	}

	protected void setPlayerType(PlayerType playerType) {
		this.playerType = playerType;
	}
}
