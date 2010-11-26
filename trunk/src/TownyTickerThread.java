public class TownyTickerThread extends Thread {
	public boolean running = true;
	TownyThread towny;

	public TownyTickerThread(TownyThread towny) {
		super();
		this.towny = towny;
	}
	
	public void run() {
		try {
			while (running) {
				if (TownyProperties.townRegen > 0) {
					for (Player player : etc.getServer().getPlayerList()) {
						Resident resident = towny.world.residents.get(player.getName());
						if (resident == null) continue;
						if (resident.town == null) continue;
						long[] posTownBlock = TownyUtil.getTownBlock((long)player.getX(), (long)player.getZ());
						String key = posTownBlock[0]+","+posTownBlock[1];
						TownBlock townblock = towny.world.townblocks.get(key);
						if (townblock == null) continue;
						if (townblock.town == null) continue;
						if (townblock.town == resident.town) {
							player.setHealth(player.getHealth()+TownyProperties.townRegen);
							continue;
						}
						if (resident.town.nation == null || townblock.town.nation == null) continue;
						if (resident.town.nation == townblock.town.nation ||
							resident.town.nation.friends.contains(townblock.town.nation)) {
							player.setHealth(player.getHealth()+TownyProperties.townRegen);
							continue;
						}
					}
				}
				
				if (TownyProperties.noMobsInTown) {
					for (Mob mob : etc.getServer().getMobList()) {
						long[] posTownBlock = TownyUtil.getTownBlock((long)mob.getX(), (long)mob.getZ());
						String key = posTownBlock[0]+","+posTownBlock[1];
						TownBlock townblock = towny.world.townblocks.get(key);
						if (townblock == null) continue;
						if (townblock.town == null) continue;
						
						//delete mob
						//Workaround
						mob.teleportTo(mob.getX(), -50, mob.getZ(), 0, 0);
					}
				}
				
				sleep(1000);
			}
		} catch (InterruptedException e) {}
	}
}