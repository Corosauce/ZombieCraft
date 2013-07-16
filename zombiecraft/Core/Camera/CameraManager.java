package zombiecraft.Core.Camera;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;

import zombiecraft.Forge.ZCClientTicks;
import zombiecraft.Forge.ZCKeybindHandler;
import CoroAI.componentAI.ICoroAI;
import cpw.mods.fml.client.FMLClientHandler;

public class CameraManager {
	
	//camera for following the player third person MAYBE
	//camera for spectating when you die
	//camera for previewing the level
	
	public EnumCameraState camState = EnumCameraState.OFF;
	public EntityLiving activeCamera = null;
	public EntityLiving spectateTarget = null;
	public int spectateTargetIndex;
	public MovementInputProxy mip = null;
	public Minecraft mc = null;
	public List<CameraPoint> camPoints;
	public int targetPointIndex = 0;
	
	public float lockYaw;
	public float lockPitch;
	public float oldYaw;
	public float oldPitch;
	
	public float aimYaw;
	public float smoothYaw;
	
	//Recoil
	public float curRecoil;
	public float curRecoilForce;
	public float recoilResist;
	
	public CameraManager() {
		mc = FMLClientHandler.instance().getClient();
	}
	
	public void initCameraPoints() {
		if (ZCClientTicks.zcGame.mapMan != null) {
			if (camPoints == null) {
				camPoints = new LinkedList();
				
				//WHEN SETTING CAMERA POINT VIA EDITOR, USE PLAYERS VIEWING ANGLES TO SET THE DESIRED CAM ANGLE FOR THAT POINT!!!
				
				CameraPoint cp = new CameraPoint();
				cp.posX = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnX_world + 50;
				cp.posY = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnY_world;
				cp.posZ = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnZ_world;
				camPoints.add(cp);
				
				cp = new CameraPoint();
				cp.posX = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnX_world + 50;
				cp.posY = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnY_world;
				cp.posZ = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnZ_world + 50;
				camPoints.add(cp);
				
				cp = new CameraPoint();
				cp.posX = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnX_world;
				cp.posY = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnY_world;
				cp.posZ = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnZ_world;
				camPoints.add(cp);
			}
			
			targetPointIndex = 0;
		}
	}
	
	public void renderTick() {
		if (mc.thePlayer != null && !(mc.thePlayer.movementInput instanceof MovementInputProxy)) {
			 mip = new MovementInputProxy(mc.gameSettings);
			 mc.thePlayer.movementInput = mip;
		}
		
		if (camState != EnumCameraState.OFF) {
			float var2 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
	        float var3 = var2 * var2 * var2 * 8.0F;
	        float var4 = (float)this.mc.mouseHelper.deltaX * var3;
	        float var5 = (float)this.mc.mouseHelper.deltaY * var3;
	        byte var6 = 1;
	        activeCamera.setAngles(var4, var5 * (float)var6);
		} else {
			
			
			
		}
	}
	
	boolean isPressed = false;
	
	public void gameTick() {
		//test code
		if (mc.currentScreen == null && Keyboard.isKeyDown(ZCKeybindHandler.cameraKey.keyCode)) {
			if (!isPressed) {
				if (camState == EnumCameraState.OFF) {
					freeCam();
				} else if (camState == EnumCameraState.FREE) {
					spectate(mc.thePlayer);
				} else if (camState == EnumCameraState.FOLLOW) {
					disableCamera();
				}
				/*if (camState == EnumCameraState.OFF) {
					spectate(mc.thePlayer);
				} else if (camState == EnumCameraState.FOLLOW) {
					freeCam();
				} else if (camState == EnumCameraState.FREE) {
					startScript();
				} else if (camState == EnumCameraState.SCRIPT) {
					disableCamera();
				}*/
			}
			isPressed = true;
		} else if (mc.currentScreen == null && camState == EnumCameraState.FOLLOW && Keyboard.isKeyDown(ZCKeybindHandler.chargeKey.keyCode)) {
			if (!isPressed) {
				spectateNext();
			}
			isPressed = true;		
		} else {
			isPressed = false;
		}
		
		checkCamera(false);
		
		if (camState == EnumCameraState.OFF) {
			//Handle recoil
			
		}
		
		
		
		
		
		recoilTick();
		
		if (mc.thePlayer != null && activeCamera != null && mip != null) {
			if (camState == EnumCameraState.FOLLOW) {
				if (spectateTarget != null) {
					//OVER THE SHOULDER CODE
					/*activeCamera.setPosition(spectateTarget.posX, spectateTarget.posY - 1.2F, spectateTarget.posZ);
					activeCamera.rotationPitch = spectateTarget.rotationPitch;
					activeCamera.rotationYaw = spectateTarget.rotationYaw;
					
					float dist = 0.8F;
					float angle = activeCamera.rotationYaw + 75;
					
					activeCamera.posX += (Math.sin(angle * Math.PI / 180) * dist);
					activeCamera.posY += (Math.sin(activeCamera.rotationPitch * Math.PI / 180) * dist);
					activeCamera.posZ -= (Math.cos(angle * Math.PI / 180) * dist);*/
					
					float yOffset = 0F;
					
					if (spectateTarget instanceof EntityOtherPlayerMP || !(spectateTarget instanceof EntityPlayer)) {
						yOffset = 1F;
					}
					
					activeCamera.setPosition(spectateTarget.posX, spectateTarget.posY + 0.6F + yOffset, spectateTarget.posZ);
					//activeCamera.rotationPitch = spectateTarget.rotationPitch;
					
					//float rate = 3F;
					
					
					
					float bestMove = MathHelper.wrapAngleTo180_float(activeCamera.rotationYaw - spectateTarget.rotationYaw);
					
					float camRotateSpeed = bestMove * 0.03F;
					
					if (camRotateSpeed < 0.03F) camRotateSpeed = 0F;
					
					//System.out.println(bestMove);
					
					//if (Math.abs(bestMove) < 180 - (camRotateSpeed * 2)) {
						if (bestMove > camRotateSpeed * 1) {
							if (bestMove < camRotateSpeed * 2) {
								activeCamera.rotationYaw = spectateTarget.rotationYaw;
							} else {
								activeCamera.rotationYaw -= camRotateSpeed;
							}
						} else if (bestMove < camRotateSpeed * 1) {
							if (bestMove > camRotateSpeed * 2) {
								activeCamera.rotationYaw = spectateTarget.rotationYaw;
							} else {
								activeCamera.rotationYaw += camRotateSpeed;
							}
							
						}
						
						while (activeCamera.rotationYaw >= 180.0F) activeCamera.rotationYaw -= 360.0F;
			            while (activeCamera.rotationYaw < -180.0F) activeCamera.rotationYaw += 360.0F;
						
						//System.out.println("activeCamera.rotationYaw: " + activeCamera.rotationYaw);
					//}
					
					/*if (this.aimYaw+rate < spectateTarget.rotationYaw) {
						this.aimYaw += rate;
					} else if (this.aimYaw-rate > spectateTarget.rotationYaw) {
						this.aimYaw -= rate;
					}
					activeCamera.rotationYaw = this.aimYaw;*/
					
					float dist = 0.5F;
					float angle = activeCamera.rotationYaw - 0;
					
					activeCamera.posX += (Math.sin(angle * Math.PI / 180) * dist);
					//activeCamera.posY += (Math.sin(activeCamera.rotationPitch * Math.PI / 180) * dist);
					activeCamera.posZ -= (Math.cos(angle * Math.PI / 180) * dist);
					
					//activeCamera.setAngles(spectateTarget.rotationPitch, spectateTarget.rotationYaw);
				}
			} else if (camState == EnumCameraState.FREE) {
				float speed = 0.4F;
				float angle = mip.camForward;
				
				//activeCamera.rotationPitch += oldPitch - mc.thePlayer.rotationPitch;
				//activeCamera.rotationYaw += oldYaw - mc.thePlayer.rotationYaw;
				//oldPitch = mc.thePlayer.rotationPitch;
				//oldYaw = mc.thePlayer.rotationYaw;
				
				mip.updatePlayerMoveState();
				
				activeCamera.moveFlying(mip.camStrafe, mip.camForward, speed);
				activeCamera.setPosition(activeCamera.posX + activeCamera.motionX, activeCamera.posY + (mip.camUp ? speed : 0F) + (mip.camDown ? -speed : 0F), activeCamera.posZ + activeCamera.motionZ);
				activeCamera.motionX = 0F;
				activeCamera.motionZ = 0F;
				
				mc.thePlayer.rotationPitch = lockPitch;
				mc.thePlayer.rotationYaw = lockYaw;
				
				//((EntityClientPlayerMP)mc.thePlayer).sendQueue.addToSendQueue(new Packet11PlayerPosition(mc.thePlayer.motionX, -999D, -999D, mc.thePlayer.motionZ, true));
				
				//mc.thePlayer.setHealth(0);
				
				//activeCamera.posX += mip.camForward;//(Math.sin(angle * Math.PI / 180) * dist);
				//activeCamera.posY += (Math.sin(activeCamera.rotationPitch * Math.PI / 180) * dist);
				//activeCamera.posZ -= (Math.cos(angle * Math.PI / 180) * dist);
			} else if (camState == EnumCameraState.SCRIPT) {
				
				if (camPoints == null || targetPointIndex >= camPoints.size()) {
					this.disableCamera();
					return;
				}
				
				CameraPoint cp = camPoints.get(targetPointIndex);
				
				float dist = (float)activeCamera.getDistance(cp.posX, cp.posY, cp.posZ);
				
				double vecX = cp.posX - activeCamera.posX;
	            double vecY = cp.posY/* + (double)(par1Entity.height / 2.0F)*/ - (activeCamera.posY + (double)(activeCamera.height / 2.0F));
	            double vecZ = cp.posZ - activeCamera.posZ;
	            
	            float var16 = MathHelper.sqrt_double(vecX * vecX + vecZ * vecZ);
	            
	            float targYaw = (float)(Math.atan2(vecX, vecZ) * 180.0D / Math.PI);
	            float targPitch = (float)(Math.atan2(vecY, (double)var16) * 180.0D / Math.PI);;

	            while (targPitch >= 180.0F) targPitch -= 360.0F;
	            while (targPitch < -180.0F) targPitch += 360.0F;
	            while (targYaw >= 180.0F) targYaw -= 360.0F;
	            while (targYaw < -180.0F) targYaw += 360.0F;
	            
	            float speed = cp.camMoveSpeed;
				float smoothYaw = targYaw;
				float smoothPitch = targPitch;
				
				activeCamera.posX += (Math.sin(smoothYaw * Math.PI / 180) * speed);
				activeCamera.posY += (Math.sin(smoothPitch * Math.PI / 180) * speed);
				activeCamera.posZ += (Math.cos(smoothYaw * Math.PI / 180) * speed);
				
				activeCamera.setPosition(activeCamera.posX, activeCamera.posY, activeCamera.posZ);
				activeCamera.motionX = 0F;
				activeCamera.motionZ = 0F;
				
				float smoothYawLook = cp.Yaw;//targYaw - 180F;
				float smoothPitchLook = cp.Pitch;//smoothPitch + 10;
				
				/*while (smoothYawLook >= 180.0F) smoothYawLook -= 360.0F;
	            while (smoothYawLook < -180.0F) smoothYawLook += 360.0F;
				while (smoothPitchLook >= 180.0F) smoothPitchLook -= 360.0F;
	            while (smoothPitchLook < -180.0F) smoothPitchLook += 360.0F;*/
				
				float camRotateSpeed = cp.camAimSpeed;
				
				float bestMove = MathHelper.wrapAngleTo180_float(smoothYawLook - activeCamera.rotationYaw);
				
				if (Math.abs(bestMove) < 180 - (camRotateSpeed * 2)) {
					if (bestMove > 0) activeCamera.rotationYaw -= camRotateSpeed;
					if (bestMove < 0) activeCamera.rotationYaw += camRotateSpeed;
				}
				
				if (activeCamera.rotationPitch > smoothPitchLook) activeCamera.rotationPitch -= camRotateSpeed;
				if (activeCamera.rotationPitch < smoothPitchLook) activeCamera.rotationPitch += camRotateSpeed;
				
	            if (dist < 1F) {
	            	targetPointIndex++;
	            }
			}
		}
	}
	
	public void checkCamera(Boolean forcePosUpdate) {
		if (activeCamera == null && mc.thePlayer != null) {
			EntityCamera cam = newCamera(mc.thePlayer);
			cam.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2F, mc.thePlayer.posZ);
			cam.setAngles(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
			mc.thePlayer.worldObj.spawnEntityInWorld(cam);
			mc.thePlayer.worldObj.playerEntities.remove(cam);
			activeCamera = cam;			
		} else {
			//System.out.println("Didnt make camera");
		}
		if (forcePosUpdate && activeCamera != null) {
			activeCamera.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2F, mc.thePlayer.posZ);
			activeCamera.setAngles(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
		}
	}
	
	public void disableCamera() {
		if (activeCamera != null) {
			activeCamera.setDead();
			activeCamera = null;
		}
		mc.renderViewEntity = mc.thePlayer;
		camState = EnumCameraState.OFF;
	}
	
	public void spectate(EntityLiving entity) {
		checkCamera(true);
		
		spectateTarget = entity;
		activeCamera.setPosition(entity.posX, entity.posY, entity.posZ);
		activeCamera.setAngles(entity.rotationYaw, entity.rotationPitch);
		mc.renderViewEntity = activeCamera;
		camState = EnumCameraState.FOLLOW;
	}
	
	public void spectateNext() {
		
		//spectateTargetIndex++;
		int safetyBreak = 0;
		
		System.out.println("mc.theWorld.playerEntities.size(): " + mc.theWorld.playerEntities.size());
		
		try {
			
			//TEMP!!!!!!!!! - remove the import too
			for (int i = 0; i < mc.theWorld.loadedEntityList.size(); i++) {
				Entity ent = (Entity)mc.theWorld.loadedEntityList.get(i);
				if (ent instanceof ICoroAI && ((EntityLiving)ent).getMaxHealth() == 50 && ent.worldObj.rand.nextInt(10) == 0) {
					spectate((EntityLiving)ent);
					spectateTargetIndex = i;
					return;
				}
			}
			
			if (mc.theWorld.playerEntities.size() > 1) {
				if (spectateTargetIndex > mc.theWorld.playerEntities.size()) spectateTargetIndex = 0;
				
				for (int i = spectateTargetIndex + 1; i != spectateTargetIndex; i++) {
					if (safetyBreak++ > 100) return;
					if (i >= mc.theWorld.playerEntities.size()) i = 0;
					EntityPlayer entP = (EntityPlayer)mc.theWorld.playerEntities.get(i);
					System.out.println("what: " + i + " - " + entP + " - h: " + entP.getHealth());
					if (!((EntityPlayer)spectateTarget).username.equals(entP.username) && !entP.isDead && entP.deathTime == 0 && entP.getHealth() > 1) {
						System.out.println("Spectating: " + entP.username);
						spectate(entP);
						spectateTargetIndex = i;
						return;
					}
					
					
				}
			}
			
			
			
		} catch (Exception ex) { 
			//ex.printStackTrace();
		}
		
		spectateTargetIndex = -1;
		
	}
	
	public void freeCam() {
		checkCamera(true);
		
		lockYaw = mc.thePlayer.rotationYaw;
		lockPitch = mc.thePlayer.rotationPitch;
		oldYaw = 0F;
		oldPitch = 0F;
		mc.renderViewEntity = activeCamera;
		camState = EnumCameraState.FREE;
	}
	
	public void startScript() {
		initCameraPoints();
		camState = EnumCameraState.SCRIPT;
	}
	
	public EntityCamera newCamera(EntityLiving entity) {
		EntityCamera cam = new EntityCamera(entity.worldObj);
		return cam;
	}
	
	public void recoilTick() {
		
		if (mc.thePlayer == null/* || mc.renderViewEntity instanceof EntityCamera*/) return;
		
		recoilResist = 1F;
		
		float adj = 0F;
		if (curRecoil + curRecoilForce < 0) {
			adj = (curRecoil + curRecoilForce) * -1F;
		}
		curRecoil += curRecoilForce - adj;
		mc.thePlayer.rotationPitch -= curRecoilForce + adj;
		
		if (curRecoil > 0) {
		
			curRecoilForce -= recoilResist;
			
			
			
			//float amount = 2F;
			//curRecoil -= amount;
			//if (curRecoil < 0) amount = amount - curRecoil;
			
		} else {
			curRecoilForce = 0;
			curRecoil = 0; //evening out
		}
	}
	
	public void recoil(float amount) {
		//if (curRecoil <= 0) {
			//mc.thePlayer.rotationPitch -= amount;
			curRecoilForce += (amount * 0.4F);
		//}
	}
	
}
