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
				
				sleep(1000);
			}
		} catch (InterruptedException e) {}
	}
}