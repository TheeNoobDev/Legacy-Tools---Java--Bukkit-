package net.chefcraft.legacytools.utils;

import org.bukkit.Bukkit;

public enum NNVersion {

	v1_8_R3(47),
	v1_9_R1(109),
	v1_9_R2(110),
	v1_10_R1(210),
	v1_11_R1(316),
	v1_12_R1(340),
	v1_13_R1(401),
	v1_13_R2(404),
	v1_14_R1(498),
	v1_15_R1(578),
	v1_16_R1(736),
	v1_16_R2(753),
	v1_16_R3(754),
	v1_17_R1(756),
	v1_18_R1(757),
	v1_18_R2(758),
	v1_19_R1(760),
	v1_19_R2(761),
	v1_19_R3(762),
	v1_20_R1(763),
	v1_20_R2(764),
	UNKNOWN(-1);
	
	public static final NNVersion SERVER_VERSION = NNVersion.valueOf(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
	public static final boolean IS_SERVER_LEGACY = SERVER_VERSION.isVersionLowerThan(NNVersion.v1_13_R1);
	
	private final int protocolVersion;
	
	NNVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public int getProtocolVersion() {
		return this.protocolVersion;
	}
	
	public boolean isVersionHigherThan(NNVersion version) {
		return this.protocolVersion > version.getProtocolVersion();
	}
	
	public boolean isVersionLowerThan(NNVersion version) {
		return this.protocolVersion < version.getProtocolVersion();
	}
}
