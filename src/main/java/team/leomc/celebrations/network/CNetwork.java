package team.leomc.celebrations.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import team.leomc.celebrations.Celebrations;

@EventBusSubscriber(modid = Celebrations.ID, bus = EventBusSubscriber.Bus.MOD)
public class CNetwork {
	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");
		registrar.playToServer(
			UpdateSignLikeTextPayload.TYPE,
			UpdateSignLikeTextPayload.STREAM_CODEC,
			new DirectionalPayloadHandler<>(
				UpdateSignLikeTextPayload::handle,
				UpdateSignLikeTextPayload::handle
			)
		);
		registrar.playToClient(
			OpenCoupletEditScreenPayload.TYPE,
			OpenCoupletEditScreenPayload.STREAM_CODEC,
			new DirectionalPayloadHandler<>(
				OpenCoupletEditScreenPayload::handle,
				OpenCoupletEditScreenPayload::handle
			)
		);
	}
}
