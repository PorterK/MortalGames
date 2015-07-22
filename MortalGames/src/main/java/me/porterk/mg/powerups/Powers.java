package me.porterk.mg.powerups;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum Powers
{
    DOUBLE_EMERALDS(DoubleEmeralds.class, "Double Emeralds", "Mobs drop double emeralds!", Material.EMERALD, 20),
    DOUBLE_EXP(DoubleExp.class, "Double Experience", "Mobs drop double experience!", Material.EXP_BOTTLE, 20),
    NUKE(Nuke.class, "Nuke", "With one use all mobs in a 30 block radius die!", Material.TNT, 15),
    ULTRA_DAMAGE(UltraDamage.class, "Ultra Damage", "Mobs die in a single hit!", Material.DIAMOND_SWORD, 20);

    private Class<? extends Powerup> pClass;
    private String                   name;
    private Material                 shop;
    private String                   desc;
    private int                      price;

    private Powers(Class<? extends Powerup> pClass, String name, String desc, Material shop, int price)
    {
        this.pClass = pClass;
        this.name = name;
        this.desc = desc;
        this.shop = shop;
        this.price = price;
    }

    public Material getShopItem()
    {
        return shop;
    }

    public String getName()
    {
        return name;
    }

    public Class<? extends Powerup> Class()
    {
        return pClass;
    }

    public String getDescription()
    {
        return desc;
    }

    public int getPrice()
    {
        return price;
    }

    public void start(Player p)
    {
        Method m;
        try
        {
            m = pClass.getMethod("start", Player.class);
            try
            {
                m.invoke(pClass.newInstance(), p);
            } catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            } catch (InvocationTargetException e)
            {
                e.printStackTrace();
            } catch (InstantiationException e)
            {
                e.printStackTrace();
            }

        } catch (SecurityException e)
        {
            e.printStackTrace();
        } catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }
}