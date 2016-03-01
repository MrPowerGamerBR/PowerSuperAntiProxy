package com.mrpowergamerbr.powersuperantiproxy.utils;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import com.mrpowergamerbr.powersuperantiproxy.PowerSuperAntiProxy;

public class AsrielConfig {
	/*
	 * Asriel "Dreemurr" Config - A way "like a dream" to load values from config without setting values!
	 *  
	 *                                        NMMMMMMM``+MMMMM                
     *                                   /yyMMm/////  /MMMMN//yMM.                                    
     *                                `//hMMhyo       /MMhyy//yMM.                                    
     *                           `````-MMNNN`         /NN- `NNNMM-`````                               
     *                           /NNNNNMMo``           ``   ``oMMNNNNN/                               
     *                      +yyyydMMo////.                    .////oMMdyyyy+                          
     *                   `//dMMyyyyy.                              .yyyyyMMd//`                       
     *                 ..+MMNmm                                          mmNMM+..                     
     *                 mmNMM+..                                          ..+MMNmm                     
     *              /yyMMd//`                                              `//dMMyy/                  
     *           `//hMMhy+                  :////.    .////:                  +yhMMh//`               
     *           -MMNmm`         `..........dmmmm/    /mmmmd..........`         `mmNMM-               
     *           -MMo..          /mmmmmmmmmm-....`    `....-mmmmmmmmmm/          ..oMM-               
     *           -MM+            .//////////                //////////.            +MM-               
     *         :/oMM+               :///////                ///////:               +MMo/:             
     *         mMMmm/            `..hmNMMMMM-.`          `.-MMMMMNmh..`            /mmMMm             
     *         mMd..`            /mm:.:MMMMMmmy          ymmMMMMM:.:mm/            `..dMm             
     *         mMd          +yy  .//` .MMMMM+/-          -/+MMMMM. `//.  yy+          dMm             
     *         mMd          hMN       .MMMMM`              `MMMMM.       NMh          dMm             
     *         mMd          hMN     ``-NNNNN`              `NNNNN-``     NMh          dMm             
     *         mMd          hMN     hmd`````                `````dmh     NMh          dMm             
     *         mMd          hMMyy/  :/:                          :/:  /yyMMh          dMm             
     *         mMd          hMMMMs            `//.    .//`            sMMMMh          dMm             
     *         mMd          hMMMMs            -NN/    /NN-            sMMMMh          dMm             
     *         mMd          hMMMMs             ``      ``             sMMMMh          dMm             
     *         mMd          hMMMMmyy.    /yy`              `yy/    .yymMMMMh          dMm             
     *         mMd          hMMMMMMM-    /yy//-  -////-  -//yy/    -MMMMMMMh          dMm             
     *         mMd          yMMMMMMM-       mMh  yMMMMy  hMm       -MMMMMMMy          dMm             
     *         mMMMM+         `MMMMMMMm       :MMMMMMMMMM:       mMMMMMMM`            dMm             
     *         :/oMM+         `MMMMMMMNyy:    `//////////`    :yyNMMMMMMM`         :yyNMm             
     *           -MM+          yydMMhyhMMo                    oMMhyhMMdyy          +MMhys             
     *           -MM+            +MM-`-MMo                    oMM-`-MM+            +MM-               
     *           -MMMNN`         +MMMNMMMo    :NNNNNNNNNN:    oMMMNMMM+         `NNMMM-               
     *           `//hMM`         +MMMMN//.    `//////////`    .//NMMMM+         `MMh//`               
     *           `//hMM//-     //yMMhys//.                    .//syhMMy//       `MMh//`               
     *           -MMNNNNNy````.MMMMM:`-NNo`````          `````oNN-`:MMMMM.``````.NNNMM:``             
     *           -MMo````/NNNNNMMMMMNNd``oNNNNy          yNNNNo``dNNMMMMMNNNNNNNm``oMMNNd             
     *         syhMM+  yys//dMMMMMMMMMNyyo////+yyyyyyyyyy+////oyyNMMMMMMM//sMMMMMyyo//mMm             
     *         mMNyyo//MMd//syyMMMMMMMMMMy////+yyyyyyyyyy+////yMMMMMMMMMM` :MMMMMMMs  dMm             
     *      `..mMd  ommMMMMM/ `MMMMMMMMMMMMMMMd..........dMMMMMMMMMMMMMMM` :MMMMMmmo  dMm..`          
     *      +mmMMd  `..NMMMM: `MMMMMMMMMMMMMMMMmmmmmmmmmmMMMMMMMMMMMMMMMM` :MMMMN..`  dMMmm+          
     *      oMM+/+yyyyy//sMMdyy//yMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMy//yydMMs//yyyyy+/+MMo          
     *    //yMM. -MMMMM//+yyNMN  :yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:  NMNyy+//MMMMM- .MMy//        
     *   `MMNmm. -mmNMMMMh..ymm                                          mmy..hMMMMNmm- .mmNMM`       
     *   `MMs..   ..yMMMMNmm/..                                          ../mmNMMMMy..   ..sMM`       
     *   `MMo  syo  -//MMd//syyyy/                                         `//dMM//-  oys  oMM`       
     *   `MMo  mMm/////yy+  +yhMMh////////////////////////////////////.  :/-  +yy/////mMm  oMM`       
     * `.-MMo  mMMMMMMM` `..` `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMo..NMh  `..MMMMMMMm  oMM-.`     
     * ymmMMo  mMMMMMMM` :mm: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNmmMMh  smmMMMMMMMm  oMMmmy     
     * hMN//.  :/oMMMMM` :MM: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM//+yy+//MMMMMo/:  .//NMh     
     * hMN  .////+yymMM` :MM:  yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy  :MM: `MMmyy+////.  NMh     
     * hMN  oMMMMd..smm` :MM:                                              :MM: `mms..dMMMMo  NMh     
     * hMN  oMMMMMmm+..  :MM:                                              :MM:  ..+mmMMMMMo  NMh     
     * hMN  oMMMMMMMdyy  :MM:  yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy  :MM:  yydMMMMMMMo  NMh     
     * hMN  :yyyyhMMMMM` :MM: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM` :MM: `MMMMMhyyyy:  NMh     
     * hMN  `````:NNNNN` :MM: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM` :MM: `NNNNN:`````  NMh     
     * hMN  +NNNNh`````  /MM: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM` :MM/  `````hNNNN+  NMh     
     * hMN  oMMMMNyy:  yydMM:  //////////////////////////////////////////  :MMdyy  :yyNMMMMo  NMh     
     * hMN  oMMMMMMM+  NMMMM:                                              :MMMMN  +MMMMMMMo  NMh     
     * hMN``oNNNNNNN+``NMMMM:  ``````````````````````````````````````````  :MMMMN``+NNNNNNNo``NMh     
     * hMMNNo       sNNMMMMM: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM` :MMMMMNNs       oNNMMh     
     * hMN//-       -//MMMMM: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM` :MMMMM//-       -//NMh     
     * hMN             NMMMM: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM` :MMMMN             NMh     
     * hMN             NMMMM: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM` :MMMMN             NMh     
     * hMN             NMMMM:                              .MMMMMMMMMMMMM`    hMN             NMh     
     * hMN       .yy:  NMd//`                               /////////////     hMN  :yy.       NMh     
     * hMN//-    -MMy//MMh     //////////.                                    hMM//yMM-    -//NMh     
     * hNNMMs``  -MMNNNMMh  ``.MMMMMMMMMMo````````````````````````````        hMMNNNMM-  ``sMMNNh     
     * ``.MMMNN. -MM+``NMh  yNNMMMMMMMMMMMNNNNNNNNNNNNNNNNNNNNNNNNNNNN/       hMN``+MM- .NNMMM.``     
     *    //yMMhyhMM+  NMNyy+//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMdyy     hMN  +MMhyhMMy//        
     *      :yyyyyyy:  yydMM: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM` .//dMN  :yyyyyyy:          
     *                   /MM: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM` :MMNNm                     
     *                   :MM: `MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM` :MM/``                     
     *                   :MM: `MMMMMMMMMMMMMMMMMMMMM//sMMMMMMMMMMMMMMMMMM` :MM:                       
     *                   :MM: `MMMMMMMMMMMMMMMMMMdyy//+yyNMMMMMMMMMMMMMMM` :MM:                       
     *                   :MM: `MMMMMMMMMMMMMMMMMM/  MMy  hMMMMMMMMMMMMMMM` :MM:                       
     *                   :MM: `MMMMMMMMMMMMMMMMMM/  MMy  hMMMMMMMMMMMMMMM` :MM:                       
     *                   :MM: `MMMMMMMMMMMMMMMMMM/  MMy  hMMMMMMMMMMMMMMM` :MM:                       
     *                   :MM: `MMMMMMMMMMMMMMMMMM/  MMy  hMMMMMMMMMMMMMMM` :MM:                       
     *                   :MM: `MMMMMMMMMMMMMMMMMM/  MMy  hMMMMMMMMMMMMMMM` :MM:                       
     *                   :MM: `MMMMMMMMMMMMMMMMMM/  MMy  hMMMMMMMMMMMMMMM` :MM:                       
     *                   :MM: `MMMMMMMMMMMMMMMMMM/  MMy  hMMMMMMMMMMMMMMM` :MM:                       
     *                   :MM: `MMMMMMMMMMMMMMMMMM/  MMy  hMMMMMMMMMMMMMMM` :MM:                       
     *              `....+MM: `MMMMMMMMMMMMMNmmmm/  MMy  ymNMMMMMMMMMMMMM` :MM+....`                  
     *              ommmmNMM: `MMMMMMMMMMMMM-....`  MMy  `.-MMMMMMMMMMMMM` :MMNmmmmo                  
     *           .yymMM/////`  /////////////     +yyMMy     /////////////  `/////MMmyy.               
     *         :/oMMdyy                          yMMMMy                          yydMMo/:             
     *         mMMNN/                            yMMMMy                            /NNMMm             
     *         mMd```                            yMMMMy                            ```dMm             
     *         mMd  /yy     +yy               .yymMMMMmyy.               yy+     yy/  dMm             
     *         mMd  sMM`    hMN       `///////oMMdyyyydMMo///////`       NMh    `MMs  dMm             
     *         mMd``sMM`````hMN```````-MMMMMMMMMM+````+MMMMMMMMMM-```````NMh`````MMs``dMm             
     *         mMMNNMMMNNNNNMMMNNNNNNNNMMMMMMMMMMMNNNNMMMMMMMMMMMNNNNNNNNMMMNNNNNMMMNNMMm             
     *         :/oMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMo/:             
     *                 yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy.               
     *                                                                                               
     *                                                                                               
	 * Created by MrPowerGamerBR 2016
	 */
	PowerSuperAntiProxy m;
	
	public AsrielConfig(PowerSuperAntiProxy m) {
		this.m = m;
	}
	
	HashMap<String, Object> cache = new HashMap<String, Object>();
	
	public Object get(String value) {
		if (cache.containsKey(value)) {
			return cache.get(value);
		} else {
			cache.put(value, m.getConfig().get(value));
			return cache.get(value);
		}
	}

	public Object get(String value, FileConfiguration fc) {
		if (cache.containsKey(value)) {
			return cache.get(value);
		} else {
			cache.put(value, fc.get(value));
			return cache.get(value);
		}
	}
	
	public String getChanged(String value) {
		if (cache.containsKey(value)) {
			return ChatColor.translateAlternateColorCodes('&', (String) cache.get(value));
		} else {
			cache.put(value, m.getConfig().get(value));
			return ChatColor.translateAlternateColorCodes('&', (String) cache.get(value));
		}
	}
	
	public String getChanged(String value, FileConfiguration fc) {
		if (cache.containsKey(value)) {
			return ChatColor.translateAlternateColorCodes('&', (String) cache.get(value));
		} else {
			cache.put(value, fc.get(value));
			return ChatColor.translateAlternateColorCodes('&', (String) cache.get(value));
		}
	}
	
	public void resetToReload() {
		m.reloadConfig();
		
		cache.clear();
	}
}
