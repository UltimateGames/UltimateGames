UltimateGames
=============

A Minigame Engine for the Bukkit Minecraft server API.

Website: [http://dev.bukkit.org/bukkit-plugins/ultimategames/](http://dev.bukkit.org/bukkit-plugins/ultimategames/)

Minigame Repositories: [http://git.tbdnetwork.net/public/projects](http://git.tbdnetwork.net/public/projects)

Features
--------

The goal is to make creating complex minigames as simple as possible.

UG achieves this with a simple API for minigames, as well as many managers, utilities, and templates for common game components.

The License
-----------

UltimateGames is licensed under the [GNU Lesser General Public License Version 3](https://github.com/ampayne2/UltimateGames/blob/master/LICENSE.txt)

Using in your project
---------------------

If you're using [Maven](http://maven.apache.org/download.html) to manage project dependencies, simply include the following in your `pom.xml`:

    <dependency>
      <groupId>me.ampayne2</groupId>
      <artifactId>ultimategames-api</artifactId>
      <version>1.0-SNAPSHOT</version>
      <scope>provided</scope>
    </dependency>

If you do not already have repo.greatmancode.com in your repository list, you will need to add this as well:

    <repository>
      <id>greatman-repo</id>
      <url>http://repo.greatmancode.com/content/groups/public/</url>
    </repository>
		
If your goal is to create a minigame, you will need to create a class extending GamePlugin and a gameplugin.yml file.

To learn how to use various parts of UG, look at the example games on the [minigame repositories](http://git.tbdnetwork.net/public/projects).

Compiling the source
--------------------

UltimateGames uses Maven to handle its dependencies.

* Download and install [Maven 3](http://maven.apache.org/download.html)  
* Checkout this repo and run: `mvn clean install`

Contributing
------------

Guidelines:
* All new files must include the UG license header. This can be done automatically with Maven by running mvn clean.
* Generally follow the Oracle code conventions and the current formatting of UG.
* Use four spaces for indentation, not tabs.
* No trailing whitespace (spaces/tabs on new lines and end of lines).
* 200 column limit for readability.

Contact ampayne2 or greatman321 for more information regarding contributing.
