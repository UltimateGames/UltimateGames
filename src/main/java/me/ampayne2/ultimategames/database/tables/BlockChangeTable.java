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
    @Field
    public Boolean needsBlockSupport;

}
