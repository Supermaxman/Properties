package me.supermaxman.properties;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.block.Biome;

public class PlotGenerator extends ChunkGenerator {

	@SuppressWarnings("deprecation")
	PlotGenerator(int path, int plotx, int plotz, int wysokosc, int podzialx, int podzialz, String pathMaterial, String plotMaterial, String podMaterial) {
        this.walkSize = path;
        this.plotSizeX = plotx * podzialx + podzialx - 1 + 2;
        this.plotSizeZ = plotz * podzialz + podzialz - 1 + 2; 
        this.plotx = plotx;
        this.plotz = plotz;
        this.height = wysokosc;
        this.materialSciezka = Material.getMaterial(pathMaterial).getId();
        this.materialDzialka = Material.getMaterial(plotMaterial).getId();
        this.underMaterial = Material.getMaterial(podMaterial).getId();
        this.material = Material.WOOD.getId();
        
    }
    private Biome kolorafaGen = Biome.PLAINS;
    private int plotSizeX;
    private int plotSizeZ;
    private int walkSize;
    private int plotx;
    private int plotz;
    private int material;
    private int materialDzialka;
    private int materialSciezka;
    private int underMaterial;
    private int height; //7 + Math.abs(chunkx + chunkz) % 10;

    private int coordsToInt(int x, int y, int z) {
        return (x * 16 + z) * 256 + y;
    }

    /*
     * private int coords(int x, int y, int z) { return x & y & z; }
     */
    @SuppressWarnings("deprecation")
	@Override
    public byte[] generate(World world, Random rand, int chunkx, int chunkz) {
        byte[] blocks = new byte[65536];
        int x, y, z;
        //int height = 60; //7 + Math.abs(chunkx + chunkz) % 10;
        int wysokoscPorzadana = height - 2;


        int powtarzanieX = plotSizeX + walkSize;
        int powtarzanieZ = plotSizeZ + walkSize;

        for (x = 0; x < 16; ++x) {
            for (z = 0; z < 16; ++z) {
                blocks[coordsToInt(x, 0, z)] = (byte) Material.BEDROCK.getId();
                world.setBiome(x, z, kolorafaGen);
            }
        }
        for (y = 1; y < wysokoscPorzadana; ++y) {
            for (x = 0; x < 16; ++x) {
                for (z = 0; z < 16; ++z) {
                    blocks[coordsToInt(x, y, z)] = (byte) underMaterial;
                }
            }
        }

        long realX = chunkx * 16;
        long realZ = chunkz * 16;
        if (realX < 0) {
            realX = powtarzanieX + (realX % powtarzanieX);
        }
        if (realZ < 0) {
            realZ = powtarzanieZ + (realZ % powtarzanieZ);
        }
        for (x = 0; x < 16; ++x) {
            for (z = 0; z < 16; ++z) {
                long relx = (realX + x) % powtarzanieX;
                long relz = (realZ + z) % powtarzanieZ;
                if (relx < plotSizeX && relz < plotSizeZ) {
                    blocks[coordsToInt(x, wysokoscPorzadana, z)] = (byte) materialDzialka;                    
                } else {
                    blocks[coordsToInt(x, wysokoscPorzadana, z)] = (byte) materialSciezka;
                }
                if (relx % (plotx + 1) == 0 && relx < plotSizeX && relz < plotSizeZ) {  //kreski
                    blocks[coordsToInt(x, wysokoscPorzadana, z)] = (byte) material;
                }
                if (relz % (plotz + 1) == 0 && relx < plotSizeX && relz < plotSizeZ) {  //kreski
                    blocks[coordsToInt(x, wysokoscPorzadana, z)] = (byte) material;
                }

                if(relx == 1 && relz == 1) {
                	//corner
                    String s = Properties.makeString(new Location(world, (x+realX), wysokoscPorzadana+1, (z+realZ)));
                    
                    if(!Properties.available.contains(s)) {
                        Properties.available.add(s);
                    }else {
                    	
                    }
                }
                if(relx == plotSizeX-2 && relz == plotSizeZ-2) {
                    //corner
                }
                //slabs
                if (relx == 0 && relz < plotSizeZ) {
                    blocks[coordsToInt(x, wysokoscPorzadana + 1, z)] = (byte) 44;
                }
                if (relx == plotSizeX - 1 && relz < plotSizeZ) {
                    blocks[coordsToInt(x, wysokoscPorzadana + 1, z)] = (byte) 44;
                }
                if (relx < plotSizeX && relz == 0) {
                    blocks[coordsToInt(x, wysokoscPorzadana + 1, z)] = (byte) 44;
                }
                if (relx < plotSizeX && relz == plotSizeZ - 1) {
                    blocks[coordsToInt(x, wysokoscPorzadana + 1, z)] = (byte) 44;
                }
                //lanterns            
                if (relx % (plotx + 1) == 0 && relz == 0 && relx < plotSizeX && relz < plotSizeZ) {
                    blocks[coordsToInt(x, wysokoscPorzadana + 1, z)] = (byte) 43;
                    blocks[coordsToInt(x, wysokoscPorzadana + 2, z)] = (byte) Material.FENCE.getId();
                    blocks[coordsToInt(x, wysokoscPorzadana + 3, z)] = (byte) Material.FENCE.getId();
                    blocks[coordsToInt(x, wysokoscPorzadana + 4, z)] = (byte) Material.GLOWSTONE.getId();
                }
                if (relx % (plotx + 1) == 0 && relz == plotSizeZ - 1 && relx < plotSizeX && relz < plotSizeZ) {
                    blocks[coordsToInt(x, wysokoscPorzadana + 1, z)] = (byte) 43;
                    blocks[coordsToInt(x, wysokoscPorzadana + 2, z)] = (byte) Material.FENCE.getId();
                    blocks[coordsToInt(x, wysokoscPorzadana + 3, z)] = (byte) Material.FENCE.getId();
                    blocks[coordsToInt(x, wysokoscPorzadana + 4, z)] = (byte) Material.GLOWSTONE.getId();
                }
                if (relz % (plotz + 1) == 0 && relx == 0 && relx < plotSizeX && relz < plotSizeZ) {
                    blocks[coordsToInt(x, wysokoscPorzadana + 1, z)] = (byte) 43;
                    blocks[coordsToInt(x, wysokoscPorzadana + 2, z)] = (byte) Material.FENCE.getId();
                    blocks[coordsToInt(x, wysokoscPorzadana + 3, z)] = (byte) Material.FENCE.getId();
                    blocks[coordsToInt(x, wysokoscPorzadana + 4, z)] = (byte) Material.GLOWSTONE.getId();
                }
                if (relz % (plotz + 1) == 0 && relx == plotSizeX - 1 && relx < plotSizeX && relz < plotSizeZ) {
                    blocks[coordsToInt(x, wysokoscPorzadana + 1, z)] = (byte) 43;
                    blocks[coordsToInt(x, wysokoscPorzadana + 2, z)] = (byte) Material.FENCE.getId();
                    blocks[coordsToInt(x, wysokoscPorzadana + 3, z)] = (byte) Material.FENCE.getId();
                    blocks[coordsToInt(x, wysokoscPorzadana + 4, z)] = (byte) Material.GLOWSTONE.getId();
                }
            }
        }
        return blocks;
    }

    @Override
    public org.bukkit.Location getFixedSpawnLocation(World world, Random random) {
        return new org.bukkit.Location(world, 0, height, 0);
    }
}