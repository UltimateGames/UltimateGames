/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames API.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.api.message;

/**
 * An enum of the messages in the main UG plugin.
 */
public enum UGMessage implements Message {
    PREFIX("Prefix", "&8[&bUltimateGames&8] "),
    GAME_PREFIX("GamePrefix", "&8[&b%s&8] "),
    CHAT("Chat", "&8[%s&8]&f %s"),

    NO_PERMISSION("Permissions.NoPermission", "&4You do not have permission to use this command."),
    CANT_LEAVE("CantLeave", "&4Not in an arena or queue."),

    LOBBY_SET("Lobby.Set", "Set the lobby at your location."),
    LOBBY_TELEPORT("Lobby.Teleport", "Teleported to the main lobby."),

    GAME_DOESNTEXIST("Game.DoesntExist", "&4Game doesn't exist."),

    ARENA_SELECT("Arena.Select", "Punch two corners of a region to create the Arena '%s' of Game '%s'."),
    ARENA_DESELECT("Arena.Deselect", "No longer selecting a region."),
    ARENA_CREATE("Arena.Create", "Created Arena '%s' of Game '%s'."),
    ARENA_DELETE("Arena.Delete", "Removed Arena '%s' of Game '%s'."),
    ARENA_EDITON("Arena.EditOn", "Entered Arena editing mode."),
    ARENA_EDITOFF("Arena.EditOff", "No longer editing Arena."),
    ARENA_START("Arena.Start", "The game has started!"),
    ARENA_BEGIN("Arena.Begin", "The game has begun!"),
    ARENA_END("Arena.End", "The game has ended!"),
    ARENA_SETSTATUS("Arena.SetStatus", "Set the status of Arena '%s' of Game '%s' to '%s'."),
    ARENA_FORCESTART("Arena.ForceStart", "Force started the Arena '%s' of Game '%s'."),
    ARENA_FORCESTOP("Arena.ForceStop", "Force stopped the Arena '%s' of Game '%s'."),

    ARENA_JOIN("Arena.Join", "%s joined the game! %s"),
    ARENA_LEAVE("Arena.Leave", "%s left the game! %s"),
    ARENA_KICK("Arena.Kick", "You were kicked from the arena."),

    ARENA_LEAVE_REGION("Arena.LeaveRegion", "&4You are not allowed to go outside the arena."),
    ARENA_ALREADYEXISTS("Arena.AlreadyExists", "&4Arena already exists."),
    ARENA_DOESNTEXIST("Arena.DoesntExist", "&4Arena doesn't exist."),
    ARENA_FAILEDTOCREATE("Arena.FailedToCreate", "&4Failed to create Arena '%s' of Game '%s'. %s"),
    ARENA_FAILEDTOJOIN("Arena.FailedToJoin", "&4Failed to join Arena '%s' of Game '%s'. %s"),
    ARENA_NOTIN("Arena.NotIn", "&4Not in an Arena."),
    ARENA_ALREADYIN("Arena.AlreadyIn", "&4Already in an Arena."),
    ARENA_ALREADYSPECTATING("Arena.AlreadySpectating", "&4Already spectating an Arena."),
    ARENA_NOTLOADED("Arena.NotLoaded", "&4Arena not loaded."),

    TEAM_JOIN("Team.Join", "&4Joined Team %s."),
    TEAM_LEAVE("Team.Leave", "&4Left Team %s."),
    TEAM_KICK("Team.Kick", "&4Kicked from Team %s."),
    TEAM_FULL("Team.Full", "&4Team %s is full!"),
    TEAM_FRIENDLYFIRE("Team.FriendlyFire", "&4Friendly Fire is off for your Team."),
    TEAM_DOESNTEXIST("Team.DoesntExist", "&4Team doesn't exist."),

    CLASS_JOIN("Class.Join", "&4Joined Class %s."),
    CLASS_NEXTDEATH("Class.NextDeath", "&4Joined Class %s! Your inventory will be reset next death."),
    CLASS_NOACCESS("Class.NoAccess", "&4Can't join class %s! You don't have access!"),

    QUEUE_JOIN("Queue.Join", "Joined queue for Arena '%s' of Game '%s'. You are %s in line and will be able to join %s."),
    QUEUE_LEAVE("Queue.Leave", "Left queue for Arena '%s' of Game '%s'."),
    QUEUE_CLEAR("Queue.Clear", "All queues cleared."),
    QUEUE_CLEARARENA("Queue.ClearArena", "Queue for Arena '%s' of Game '%s' has been cleared."),

    SPAWNPOINT_SET("Spawnpoint.Set", "Created SpawnPoint for Arena '%s' of Game '%s'"),
    SPAWNPOINT_SETSPECTATOR("Spawnpoint.SetSpectator", "Set Spectator SpawnPoint for Arena '%s' of Game '%s'"),
    SPAWNPOINT_LEAVE("Spawnpoint.Leave", "&4You are not allowed to leave the spawnpoint."),

    ZONE_CREATE("Zone.Create", "Created Zone for Arena '%s' of Game '%s'"),

    COUNTDOWN_TIMELEFT_START("Countdown.TimeLeft.Start", "The game starts in %s seconds!"),
    COUNTDOWN_TIMELEFT_END("Countdown.TimeLeft.End", "The game ends in %s seconds!"),
    COUNTDOWN_ABORTED("Countdown.Aborted", "Countdown aborted."),

    COMMAND_NOTAPLAYER("Command.NotAPlayer", "&4You must be a player to use this command."),
    COMMAND_INVALIDSUBCOMMAND("Command.InvalidSubCommand", "&4%s is not a sub command of %s."),
    COMMAND_USAGE("Command.Usage", "&4Usage: %s"),

    ERROR_NUMBERFORMAT("Error.NumberFormat", "&4Value must be a positive integer."),
    ERROR_NUMBERTOOHIGH("Error.NumberTooHigh", "&4Value must be no greater than &b%s&4."),
    ERROR_NUMBERTOOLOW("Error.NumberTooLow", "&4Value must be no lower than &b%s&4."),
    ERROR_BOOLEANFORMAT("Error.BooleanFormat", "&4Value must be &bTrue&4 or &bFalse&4."),
    ERROR_RADIUSTYPEFORMAT("Error.RadiusTypeFormat", "&4Value must be &bRectangle&b, &bCircle&4, &bCube&4 or &bSphere&4.");

    private String message;
    private final String path;
    private final String defaultMessage;

    private UGMessage(String path, String defaultMessage) {
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getDefault() {
        return defaultMessage;
    }

    @Override
    public String toString() {
        return message;
    }
}
