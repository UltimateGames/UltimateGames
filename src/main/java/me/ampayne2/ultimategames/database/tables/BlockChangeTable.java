/*
 * This file is part of UltimateGames.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.database.tables;

import com.alta189.simplesave.Field;
import com.alta189.simplesave.Id;
import com.alta189.simplesave.Table;

@Table("BlockChangeTable")
public class BlockChangeTable {

    @Id
    public int id;
    @Field
    public String gameName;
    @Field
    public String arenaName;
    @Field
    public double x;
    @Field
    public double y;
    @Field
    public double z;
    @Field
    public String material;
    @Field
    public byte data;

}
