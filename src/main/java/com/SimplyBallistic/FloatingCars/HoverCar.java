package com.SimplyBallistic.FloatingCars;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface HoverCar {
	/*public ArmorStand getCar;
	public double getSpeed;
	public boolean isPublic;
	public ItemStack item;
	public boolean canFly;
	*//**
	 * Is null if car is not allowed to have an inventory
	 *//*
	public Inventory getInventory;
	*/
	public ArmorStand getCar();
	public double getSpeed();
	public double getSpaceSpeed();
	public double getShiftSpeed();
	public double getRevSpeed();
	public String getCarType();
	/**
	 * Returns null if public
	 * @return
	 */
	public UUID getOwner();
	public ItemStack getItem();
		  
	public boolean canFly();
	public Inventory getInventory();
	public String name();
	public Material getFuel();
	public int getCapacity();
	public int getFuelTime();
	public double getMaxHeight();
	/**
	 * 
	 * @return null if the car cannot jumpl, value with strength if can
	 */
   public Integer getJump();
}
