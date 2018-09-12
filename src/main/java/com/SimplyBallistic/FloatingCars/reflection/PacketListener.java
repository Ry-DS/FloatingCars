package com.SimplyBallistic.FloatingCars.reflection;

import com.SimplyBallistic.FloatingCars.FCMain;
import com.SimplyBallistic.FloatingCars.HoverCar;
import com.SimplyBallistic.FloatingCars.files.PlayerData;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;


public class PacketListener extends PacketAdapter {

    public static Map<HoverCar, Integer> fuel = new HashMap<>();
    Map<HoverCar, SteerPacket> lastInput = new HashMap<>();

	public PacketListener(Plugin plugin) {
		super(plugin,ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE/*,PacketType.Play.Server.CHAT*/);
		ProtocolLibrary.getProtocolManager().addPacketListener(this);

        new CarController(this);


    }

    public static void deleteCar(HoverCar car) {
        car.getCar().getPassengers().forEach(en -> car.getCar().removePassenger(en));
        car.getCar().remove();
        FCMain.cars.remove(car);
        CarController.hovertime.remove(car);
        if (car.getOwner() != null)
            PlayerData.setFuel(car.getOwner(), car.getCarType(), fuel.get(car));
        fuel.remove(car);
        FCMain.getInstance().getLogger().info("Car: " + car.getCarType() + " was saved and deleted successfully");


    }



    public static void deleteAll() {


        for (HoverCar car : FCMain.cars) {
            if (car.getOwner() != null)
                PlayerData.setFuel(car.getOwner(), car.getCarType(), fuel.get(car));
            FCMain.getInstance().getLogger().info("Car: " + car.getCarType() + " was saved and deleted succesfully");
            car.getCar().remove();


        }
        fuel.clear();
        CarController.hovertime.clear();
        FCMain.cars.clear();
    }

	@Override
	public void onPacketReceiving(PacketEvent e){
        if (e.getPlayer().getVehicle() == null || e.getPacket().getType() != PacketType.Play.Client.STEER_VEHICLE)
            return;
		for(HoverCar hc:FCMain.cars){

            if (e.getPlayer().getVehicle().getUniqueId().equals(hc.getCar().getUniqueId())) {
				PacketPlayInSteerVehicle packet = (PacketPlayInSteerVehicle) e.getPacket().getHandle();
                SteerPacket steerPacket = new SteerPacket(packet);
                lastInput.put(hc, steerPacket);
                if (steerPacket.isShiftPressed() && hc.canFly()) {//used to go down in flying shift. Cancelling prevents unmounting
                    e.setCancelled(true);
				}
                if (!steerPacket.isShiftPressed() && !steerPacket.isSpacePressed() && steerPacket.getForwardValue() == 0 && steerPacket.getSideValue() < 0
                        && !hc.canFly()) {

                    e.setCancelled(true);
                }
                if (steerPacket.isSpacePressed() && steerPacket.isShiftPressed())
                    e.setCancelled(true);


                break;
            }
        }


    }

}

