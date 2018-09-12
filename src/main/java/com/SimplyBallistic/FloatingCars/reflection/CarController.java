package com.SimplyBallistic.FloatingCars.reflection;

import com.SimplyBallistic.FloatingCars.FCMain;
import com.SimplyBallistic.FloatingCars.HoverCar;
import com.SimplyBallistic.FloatingCars.files.LanguageYml;
import com.SimplyBallistic.FloatingCars.files.PlayerData;
import com.google.common.collect.Sets;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by SimplyBallistic on 12/09/2018
 *
 * @author SimplyBallistic
 **/
public class CarController extends BukkitRunnable implements Listener {
    static Map<HoverCar, HoverTime> hovertime = new HashMap<>();
    private PacketListener plugin;
    private Material[] noSkip = {Material.ROSE_RED, Material.SUGAR, Material.BROWN_MUSHROOM,
            Material.RED_MUSHROOM, Material.CHORUS_FLOWER, Material.SUNFLOWER};

    public CarController(PacketListener plugin) {
        this.plugin = plugin;
        plugin.getPlugin().getServer().getPluginManager().registerEvents(this, plugin.getPlugin());
        runTaskTimer(plugin.getPlugin(), 10, 2);
    }

    @Override
    public void run() {
        Set<HoverCar> toRemove = Sets.newHashSet();

        plugin.lastInput.forEach((hc, packet) -> {
            Player player = (Player) hc.getCar().getPassengers().get(0);
            if (player == null) {
                toRemove.add(hc);
                return;
            }

            ArmorStand car = (ArmorStand) player.getVehicle();
            if (!car.getPassengers().isEmpty() && !(car.getPassengers().get(0) instanceof Player))
                return;
            if (PacketListener.fuel.get(hc) == null) {
                if (hc.getFuel() != null)
                    PacketListener.fuel.put(hc, PlayerData.getFuel(player.getUniqueId(), hc.getCarType()));
                else PacketListener.fuel.put(hc, Integer.MAX_VALUE);
            }

            boolean shift = packet.isShiftPressed();
            boolean space = packet.isSpacePressed();
            float forward = packet.getForwardValue();
            float side = packet.getSideValue();

            Vector continuesVelocity = car.getVelocity().setY(0);
            //System.out.println("Ride packet: space:"+space+" shift:"+shift+" forward:"+forward);
            if (space && shift) {
                car.setGravity(false);
                return;

            } else car.setGravity(true);
            CraftArmorStand handle = (CraftArmorStand) car;
            handle.getHandle().yaw = player.getLocation().getYaw();

            if (((!space && !shift && forward == 0) || (space && shift)) && PacketListener.fuel.get(hc) > 0) {


                hover(hc);
                car.setHeadPose(new EulerAngle(0, 0, 0));


            } else {
                car.setGravity(true);
            }
            Block b = car.getLocation().getBlock();
            if (forward > 0) {
                //System.out.println("ForwUp Triggered");
                //car.setVelocity(MovmentScheduler.genForwUpVec(car.getLocation()));
                if (PacketListener.fuel.get(hc) <= 0) player.sendTitle(
                        LanguageYml.getAndConv("no-fuel", ChatColor.RED + "No Fuel"), "", 0, 10, 10);
                else {
                    PacketListener.fuel.put(hc, PacketListener.fuel.get(hc) - 1);
                    if (!car.isOnGround())
                        continuesVelocity = car.getLocation().getDirection().multiply(hc.getSpeed());
                    else continuesVelocity = car.getLocation().getDirection().multiply(hc.getSpeed() / 1.5);

                    if (!FCMain.getInstance().getConfig().getBoolean("fly-lookup", false))
                        car.setVelocity(continuesVelocity.setY(0)/*.setY(player.getLocation().getDirection().getY())*/);
                    else car.setVelocity(continuesVelocity.setY(player.getLocation().getDirection().getY()));

                    if ((b.getRelative(0, -2, 0).getType().equals(Material.WATER)
                            || b.getRelative(0, -1, 0).getType().equals(Material.WATER)
                            || b.getType().equals(Material.WATER))

                            && hc.canHoverWater()) car.setVelocity(car.getVelocity().setY(0.05));
                    else if (!hc.canFly()) car.setVelocity(car.getVelocity().setY(-0.5));
                }
            }
            if (forward < 0) {
                if (PacketListener.fuel.get(hc) <= 0) player.sendTitle(
                        LanguageYml.getAndConv("no-fuel", ChatColor.RED + "No Fuel"), "", 0, 10, 10);
                else {
                    PacketListener.fuel.put(hc, PacketListener.fuel.get(hc) - 1);
                    if (hc.canFly())
                        continuesVelocity = car.getLocation().getDirection().multiply(-hc.getRevSpeed()).setY(0);
                    else continuesVelocity = car.getLocation().getDirection().multiply(-hc.getRevSpeed()).setY(-0.5);
                    car.setVelocity(continuesVelocity);
                }
            }

				/*if(side>0)
					car.setVelocity(car.getVelocity().setX(-0.5));
				else if(side<0)
					car.setVelocity(car.getLocation().getDirection().multiply(0.5).getCrossProduct(car.getLocation().getDirection().multiply(-0.5)));*/
            canFly:
            {
                if (space) {

                    if (!hc.canFly() && hc.getJump() != null && PacketListener.fuel.get(hc) > 0) {
                        if (car.isOnGround()) {
                            car.setVelocity(car.getVelocity().setY(hc.getSpaceSpeed()));
                            PacketListener.fuel.put(hc, PacketListener.fuel.get(hc) - hc.getJump());
                        } else break canFly;
                    }
                    if (FCMain.getInstance().getConfig().getBoolean("fly-lookup", false))
                        break canFly;
                    if (PacketListener.fuel.get(hc) <= 0) player.sendTitle(
                            LanguageYml.getAndConv("no-fuel", ChatColor.RED + "No Fuel"), "", 0, 10, 10);
                    else {
                        PacketListener.fuel.put(hc, PacketListener.fuel.get(hc) - 1);
                        if (car.getLocation().getY() > hc.getMaxHeight())
                            car.setVelocity(car.getVelocity());
                        else {
                            car.setVelocity(car.getVelocity().setY(hc.getSpaceSpeed()));
                            car.setHeadPose(new EulerAngle(Math.toRadians(-25), 0, 0));
                        }
                    }
                }
            }
            if (shift) {
                if (hc.canFly()) {
                    car.setVelocity(car.getVelocity().setY(-hc.getShiftSpeed()));
                    car.setHeadPose(new EulerAngle(Math.toRadians(25), 0, 0));

                } else return;

            }
            if (!shift && !space && forward == 0 && side < 0) {
                if (!hc.canFly()) {
                    car.eject();
                    return;
                }
                if (!FCMain.getInstance().getConfig().getBoolean("dismount-air", false)) {
                    if (car.isOnGround() || !car.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.AIR) || PacketListener.fuel.get(hc) <= 0) {
                        car.eject();
                        car.setGravity(true);
                    } else {
                        player.sendTitle(
                                LanguageYml.getAndConv("dismount-in-air-short", ChatColor.RED + "Land First!"),
                                LanguageYml.getAndConv("dismount-in-air-long", ChatColor.GREEN + "Land if you wish to dismount"), 20, 10, 20);

                    }
                } else {
                    car.eject();
                    car.setGravity(true);
                }
            }
            if (!shift && !space && forward == 0 && side > 0) {
                player.openInventory(PlayerData.getPlayerInventory(player.getUniqueId(), hc.getCarType()));
            }
            if (!(PacketListener.fuel.get(hc) <= 0) && (!hc.canFly() || car.getLocation().getY() >= hc.getMaxHeight()))
                driveOver(b, car, player);
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);
            StringBuilder meter = new StringBuilder();

            if (hc.getFuel() != null) {
                int i;
                for (i = 0; i < Math.abs((float) PacketListener.fuel.get(hc) / hc.getCapacity() * 20f); i++) {
                    meter.append(LanguageYml.getAndConv("fuel-fill", "█"));

                }
                for (; i < 20; i++) {
                    meter.append(LanguageYml.getAndConv("fuel-empty", "░"));
                }
            }

            /*df.format(Math.abs((float)fuel.get(hc)/hc.getCapacity()*100f))*/
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    LanguageYml.getAndConv("fuel-prefix", ChatColor.GOLD + "Fuel: " + ChatColor.YELLOW) + meter.toString()));
            //car.setVelocity(car.getLocation().getDirection().setY(player.getLocation().getDirection().getY()));

        });
        for (HoverCar car : toRemove) {
            plugin.lastInput.remove(car);
        }
    }

    private void driveOver(Block b, ArmorStand car, Player p) {
        Material air = Material.AIR;
        // CHECK X AXIS
        if ((b.getRelative(1, 1, 0).getType() == air && b.getRelative(1, 0, 0).getType() != air)) {
            getRelative(p, b, 1, 0, car);
        }
        // CHECK -X AXIS
        if ((b.getRelative(-1, 1, 0).getType() == air && b.getRelative(-1, 0, 0).getType() != air)) {
            getRelative(p, b, -1, 0, car);
        }
        // CHECK Z AXIS
        if ((b.getRelative(0, 1, 1).getType() == air && b.getRelative(0, 0, 1).getType() != air)) {
            getRelative(p, b, 0, 1, car);
        }
        // CHECK -Z AXIS
        if ((b.getRelative(0, 1, -1).getType() == air && b.getRelative(0, 0, -1).getType() != air)) {
            getRelative(p, b, 0, -1, car);
        }

    }

    private void getRelative(Player p, Block b, int X, int Z, ArmorStand car) {

        for (Material material : noSkip)
            if (material.equals(b.getRelative(X, 1, Z).getType()) || material.equals(b.getRelative(X, 0, Z).getType()))
                return;

        car.setVelocity(car.getVelocity().setY(0.3));


    }

    private void hover(HoverCar car) {

        hovertime.computeIfAbsent(car, k -> new HoverTime());
        hovertime.get(car).advTime(1);
        if (hovertime.get(car).hoverUp)
            car.getCar().setVelocity((car.getCar().getVelocity().setY(0.05)));
        else car.getCar().setVelocity(car.getCar().getVelocity().setY(-0.05));
        if (hovertime.get(car).time % 20 == 0) hovertime.get(car).hoverUp = !hovertime.get(car).hoverUp;


    }

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        for (HoverCar car : FCMain.cars) {
            if (car.getCar() == e.getDismounted() || car.getCar() == e.getEntity()) {
                plugin.lastInput.remove(car);
                return;
            }
        }
    }

    private class HoverTime {
        int time = 0;
        boolean hoverUp = true;

        HoverTime advTime(int time) {
            this.time += time;
            if (this.time >= 100) this.time = 0;
            return this;
        }

    }
}
