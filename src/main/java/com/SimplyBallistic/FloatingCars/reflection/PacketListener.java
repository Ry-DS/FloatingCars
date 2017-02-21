package com.SimplyBallistic.FloatingCars.reflection;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.SimplyBallistic.FloatingCars.FCMain;
import com.SimplyBallistic.FloatingCars.HoverCar;
import com.SimplyBallistic.FloatingCars.files.PlayerData;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.minecraft.server.v1_11_R1.PacketPlayInSteerVehicle;

public class PacketListener extends PacketAdapter {
	private HashMap<HoverCar, Integer>hovertime=new HashMap<>();
	public static HashMap<HoverCar, Integer>fuel=new HashMap<>();
	public PacketListener(Plugin plugin) {
		super(plugin,ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Client.STEER_VEHICLE/*,PacketType.Play.Server.CHAT*/});
		ProtocolLibrary.getProtocolManager().addPacketListener(this);
	}
	private int tickcount=0;
	private boolean hoverUp=true;
	/*@Override
	public void onPacketSending(PacketEvent e) {

		if(e.getPacketType().equals(PacketType.Play.Server.CHAT)){
			System.out.println("CALLED");
			PacketPlayOutChat packet=(PacketPlayOutChat)e.getPacket().getHandle();
			try {
				Field mess=packet.getClass().getDeclaredField("a");
				Field dat=packet.getClass().getDeclaredField("b");
				mess.setAccessible(true);
				dat.setAccessible(true);
				IChatBaseComponent cmess=(IChatBaseComponent)mess.get(packet);
				if(dat.getByte(packet)==2&&cmess.getText().contains("LSHIFT")){e.setCancelled(true);}
				
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
			
			
			
		}	
		
		
	}
	*/
	@Override
	public void onPacketReceiving(PacketEvent e){
		for(HoverCar hc:FCMain.cars){
			
			if(e.getPlayer().getVehicle()!=null&&e.getPacketType() == PacketType.Play.Client.STEER_VEHICLE 
					&& e.getPlayer().getVehicle().getUniqueId().equals(hc.getCar().getUniqueId())){
				PacketPlayInSteerVehicle packet = (PacketPlayInSteerVehicle) e.getPacket().getHandle();
				ArmorStand car = (ArmorStand) e.getPlayer().getVehicle();
				if(!car.getPassengers().isEmpty()&&!(car.getPassengers().get(0) instanceof Player))
					return;
				if(fuel.get(hc)==null)fuel.put(hc,PlayerData.getFuel(e.getPlayer().getUniqueId(), hc.getCarType()));
				
				Block b=car.getLocation().getBlock();
				tickcount++;
				boolean shift = packet.d();
				boolean space = packet.c();
				float forward = packet.b();
				float side = packet.a();
				Vector continuesVelocity=car.getVelocity();
				//System.out.println("Ride packet: space:"+space+" shift:"+shift+" forward:"+forward);
				if(space&&shift){car.setGravity(false);e.setCancelled(true);return;}
				else car.setGravity(true);
				((CraftArmorStand) car).getHandle().yaw =e.getPlayer().getLocation().getYaw();
				if(((!space&&!shift&&forward==0)||space&&shift)&&fuel.get(hc)>0){hover(car);}
				else {car.setGravity(true);}
			
				if(forward>0){
					//System.out.println("ForwUp Triggered");
					//car.setVelocity(MovmentScheduler.genForwUpVec(car.getLocation()));
					if(fuel.get(hc)<=0)FCMain.getZotLib().getPacketLibrary().getTitleManager().sendTitle(e.getPlayer(), ChatColor.RED+"No Fuel", 1, 1, 1);
					else {
					fuel.put(hc, fuel.get(hc)-1);
					if(!car.isOnGround())
					continuesVelocity=car.getLocation().getDirection().multiply(hc.getSpeed());
					else continuesVelocity=car.getLocation().getDirection().multiply(hc.getSpeed()/1.5);
					car.setVelocity(continuesVelocity/*.setY(e.getPlayer().getLocation().getDirection().getY())*/);
					}
				}
				if(forward<0){
					if(fuel.get(hc)<=0)FCMain.getZotLib().getPacketLibrary().getTitleManager().sendTitle(e.getPlayer(), ChatColor.RED+"No Fuel", 1, 1, 1);
					else{
					fuel.put(hc, fuel.get(hc)-1);
					continuesVelocity=car.getLocation().getDirection().multiply(-hc.getRevSpeed());
					car.setVelocity(continuesVelocity);
					}
				}
				
				/*if(side>0)
					car.setVelocity(car.getVelocity().setX(-0.5));
				else if(side<0)
					car.setVelocity(car.getLocation().getDirection().multiply(0.5).getCrossProduct(car.getLocation().getDirection().multiply(-0.5)));*/
				if(space){
					if(fuel.get(hc)<=0)FCMain.getZotLib().getPacketLibrary().getTitleManager().sendTitle(e.getPlayer(), ChatColor.RED+"No Fuel", 1, 1, 1);
					else{
					fuel.put(hc, fuel.get(hc)-1);
					if(car.getLocation().getY()>hc.getMaxHeight())
						car.setVelocity(car.getVelocity());
					else
					car.setVelocity(car.getVelocity().setY(hc.getSpaceSpeed()));
					}	
				}
				if(shift){
					
					car.setVelocity(car.getVelocity().setY(-hc.getShiftSpeed()));
					e.setCancelled(true);}
				if(!shift&&!space&&forward==0&&side<0){
					
					if(car.isOnGround()||!car.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.AIR)||fuel.get(hc)<=0){
					car.eject();
					car.setGravity(true);}
					else{
						FCMain.getZotLib().getPacketLibrary().getTitleManager().sendTitle(e.getPlayer(), ChatColor.RED+"Land First!", 20, 10, 20);
						FCMain.getZotLib().getPacketLibrary().getTitleManager().sendSubTitle(e.getPlayer(), ChatColor.GREEN+"Land if you wish to dismount", 20, 20, 20);
					
					}
					}
				if(!shift&&!space&&forward==0&&side>0){
				e.getPlayer().openInventory(PlayerData.getPlayerInventory(e.getPlayer().getUniqueId(),hc.getCarType()));
				}
				if(!(fuel.get(hc)<=0)&&!hc.canFly())
				driveOver(b, car, e.getPlayer());
				DecimalFormat df = new DecimalFormat("#.#");
				df.setRoundingMode(RoundingMode.CEILING);
				String meter="";
				for(int i=0;i<Math.abs((float)fuel.get(hc)/hc.getCapacity()*100f);i++){
					meter+="|";
					
				}
				/*df.format(Math.abs((float)fuel.get(hc)/hc.getCapacity()*100f))*/
				FCMain.getZotLib().getPacketLibrary().getActionBarManager().sendActionbar(e.getPlayer(), ChatColor.GOLD+"Fuel: "+ChatColor.YELLOW+meter);
				//car.setVelocity(car.getLocation().getDirection().setY(e.getPlayer().getLocation().getDirection().getY()));

			
			
		}}
		
		
		}
	@SuppressWarnings("deprecation")
	private void driveOver(Block b,ArmorStand car,Player p){
		// CHECK X AXIS
					if ((b.getRelative(1, 1, 0).getTypeId() == 0 && b.getRelative(1, 0, 0).getTypeId() != 0)) {
						getRelative(p, b, 1, 0, car);
					}
					// CHECK -X AXIS
					if ((b.getRelative(-1, 1, 0).getTypeId() == 0 && b.getRelative(-1, 0, 0).getTypeId() != 0)) {
						getRelative(p, b, -1, 0, car);
					}
					// CHECK Z AXIS
					if ((b.getRelative(0, 1, 1).getTypeId() == 0 && b.getRelative(0, 0, 1).getTypeId() != 0)) {
						getRelative(p, b, 0, 1, car);
					}
					// CHECK -Z AXIS
					if ((b.getRelative(0, 1, -1).getTypeId() == 0 && b.getRelative(0, 0, -1).getTypeId() != 0)) {
						getRelative(p, b, 0, -1, car);
					}
		
	}
	@SuppressWarnings("deprecation")
	private void getRelative(Player p, Block b, int X, int Z, ArmorStand car) {
		if (b.getRelative(X, 1, Z).getTypeId() == 38 || b.getRelative(X, 1, Z).getTypeId() == 31
				|| b.getRelative(X, 1, Z).getTypeId() == 175
				|| b.getRelative(X, 1, Z).getType().equals(Material.SUGAR_CANE_BLOCK)
				|| b.getRelative(X, 1, Z).getTypeId() == 39 || b.getRelative(X, 1, Z).getTypeId() == 40
				|| b.getRelative(X, 1, Z).getTypeId() == 37 ||

				b.getRelative(X, 0, Z).getTypeId() == 38 || b.getRelative(X, 0, Z).getTypeId() == 31
				|| b.getRelative(X, 0, Z).getTypeId() == 175
				|| b.getRelative(X, 0, Z).getType().equals(Material.SUGAR_CANE_BLOCK)
				|| b.getRelative(X, 0, Z).getTypeId() == 39 || b.getRelative(X, 0, Z).getTypeId() == 37
				|| b.getRelative(X, 0, Z).getTypeId() == 40) {
			return;

		}

		car.setVelocity(p.getLocation().getDirection().setY(0.3));
		
		return;
 
	}
	private void hover(ArmorStand car){
		tickcount++;
		if(hoverUp)
			car.setVelocity(car.getVelocity().setY(0.05));
		else car.setVelocity(car.getVelocity().setY(-0.05));
		if(tickcount>20){
			hoverUp=!hoverUp;
			tickcount=0;
		}
		
		
		
	}
}
