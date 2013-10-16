package common.captainbern.npclib.entity;

import common.captainbern.npclib.wrapper.packet.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class NPC {

    private Location location;
    private int id = 0;
    private String name;
    private ItemStack iteminhand;

    public NPC(String name, Location location){
        try{

            if(name.length() > 16){
                String tmp = name.substring(0, 16);
                Bukkit.getLogger().warning("NPC-names can't be longer then 16 chars! Changed " + name + " to: " + tmp);
                this.name = tmp;
            }else{
                this.name = name;
            }

            this.location = location;
        }catch(Exception e) {}
    }

    public void setId(int id){
        if(this.id == 0){
            this.id = id;
        }else{
            throw new RuntimeException("Cannot change entity id!");
        }
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    /**
     * Packet stuff starts here
     */

    public void spawn(){
        Packet20NamedEntitySpawn packet = new Packet20NamedEntitySpawn();
        packet.setEntityID(this.id);
        packet.setName(this.name);
        packet.setLocation(this.location);
        packet.setItemInhand(this.iteminhand);

        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getWorld().equals(this.location.getWorld())){
                packet.send(player);
            }
        }
    }

    public void despawn(){
        Packet29DestroyEntity packet = new Packet29DestroyEntity(new int[id]);
        for(Player player : Bukkit.getOnlinePlayers()){
            packet.send(player);
        }
    }

    public void swingArm(){
        Packet18ArmAnimation packet = new Packet18ArmAnimation();
        packet.setEntityID(this.id);
        packet.setAction(Packet18ArmAnimation.ACTION_SWING_ARM);
    }

    public void hurt(){
        Packet18ArmAnimation packet = new Packet18ArmAnimation();
        packet.setEntityID(this.id);
        packet.setAction(Packet18ArmAnimation.ACTION_HURT);
    }

    public void sleep(){
        Packet17EntityLocationAction packet = new Packet17EntityLocationAction();
        packet.setEntityID(this.id);
        packet.setAction(Packet17EntityLocationAction.ACTION_SLEEP);
    }

    public void wakeUp(){
        Packet18ArmAnimation packet = new Packet18ArmAnimation();
        packet.setEntityID(this.id);
        packet.setAction(Packet18ArmAnimation.ACTION_WAKE_UP);
    }

    public void teleport(Location loc){
        Packet34EntityTeleport packet = new Packet34EntityTeleport();
        //init
    }

    /*public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setItemInHand(ItemStack itemstack){
        this.iteminhand = itemstack;
        update();
    }

    public ItemStack getItemInHand(){
        return this.iteminhand;
    }

    public void setLocation(Location loc){
        this.location = loc;
        update();
    }

    public void setX(double x){
        this.location.setX(x);
        update();
    }

    public void setY(double y){
        this.location.setY(y);
        update();
    }

    public void setZ(double z){
        this.location.setZ(z);
        update();
    }

    public void setPitch(float pitch){
        this.location.setPitch(pitch);
        update();
    }

    public void setYaw(float yaw){
        this.location.setYaw(yaw);
        update();
    }

    public Location getLocation(){
        return location;
    }

    public double getX(){
        return getLocation().getX();
    }

    public double getY(){
        return getLocation().getY();
    }

    public double getZ(){
        return getLocation().getZ();
    }

    public void doArmSwing(){
        LazyPacket packet = new LazyPacket("Packet18ArmAnimation");
        packet.setPublicValue("a", this.id);
        packet.setPublicValue("b", 1);
        sendPacket(packet);
    }

    public void sleep(){
        LazyPacket packet = new LazyPacket("Packet17EntityLocationAction");
        packet.setPublicValue("a", getId());
        packet.setPublicValue("e", 0);
        packet.setPublicValue("b", (int) getLocation().getX());
        packet.setPublicValue("c", (int) getLocation().getY());
        packet.setPublicValue("d", (int) getLocation().getZ());
        sendPacket(packet);
        asleep = true;
    }

    public void wakeUp(){
        LazyPacket packet = new LazyPacket("Packet18ArmAnimation");
        packet.setPublicValue("a", getId());
        packet.setPublicValue("b", 3);
        sendPacket(packet);
        asleep = false;
    }

    public void hurt(){
        LazyPacket packet = new LazyPacket("Packet18ArmAnimation");
        packet.setPublicValue("a", getId());
        packet.setPublicValue("b", 2);
        sendPacket(packet);
    }

    public void destroy(){
        LazyPacket packet = new LazyPacket("Packet29DestroyEntity");
        packet.setPublicValue("a", new int[]{getId()});
        sendPacket(packet);
    }

    public void teleportTo(Location loc){
        LazyPacket packet = new LazyPacket("packet34EntityTeleport");
        packet.setPublicValue("a", getId());
        packet.setPublicValue("b", (int) loc.getX());
        packet.setPublicValue("c", (int) loc.getY());
        packet.setPublicValue("d", (int) loc.getZ());
        packet.setPublicValue("e", (byte) loc.getPitch());
        packet.setPublicValue("f", (byte) loc.getYaw());
        this.location = loc;
    }

    //-------------------------
    public void update(){
		/*NpcPacket p = new NpcPacket(this);
		p.setPublicValue("a", getId());
		p.setPublicValue("b", getName());
		sendPacket(p);

        LazyPacket packet = new LazyPacket("Packet20NamedEntitySpawn");
        packet.setPublicValue("a", this.id);
        packet.setPublicValue("b", this.name);
        packet.setPublicValue("c", (int) getLocation().getX() * 32);
        packet.setPublicValue("d", (int) getLocation().getY() * 32);
        packet.setPublicValue("e", (int) getLocation().getZ() * 32);
        packet.setPublicValue("f", getCompressedAngle(getLocation().getYaw()));
        packet.setPublicValue("g", getCompressedAngle(getLocation().getPitch()));
        packet.setPublicValue("h", getItemInHand() == null ? 0 : getItemInHand().getTypeId());

        DataWatcher datawatcher = new DataWatcher();
        datawatcher.write(0, (Object) (byte) 0);
        datawatcher.write(1, (Object) (short) 0);
        datawatcher.write(8, (Object) (byte) 0);
        packet.setPrivateValue("i", datawatcher.getDataWatcherObject());
    }
    //------------------

    private byte getCompressedAngle(float value) {
        return (byte) (value * 256.0F / 360.0F);
    }

    private void sendPacket(LazyPacket packet){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getLocation().distance(this.location) <= 64){
                packet.send(player);
            }
        }
    }*/
}
