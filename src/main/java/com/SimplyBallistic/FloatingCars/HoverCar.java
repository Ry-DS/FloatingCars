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
	ArmorStand getCar();
	double getSpeed();
	double getSpaceSpeed();
	double getShiftSpeed();
	double getRevSpeed();
	String getCarType();
	/**
	 * Returns null if public
	 * @return
	 */
	UUID getOwner();
	ItemStack getItem();
		  
	boolean canFly();
	Inventory getInventory();
	String name();
	Material getFuel();
	int getCapacity();
	int getFuelTime();
	double getMaxHeight();
	boolean canHoverWater();
	/**
	 * 
	 * @return null if the car cannot jumpl, value with strength if can
	 */
	Integer getJump();
}
