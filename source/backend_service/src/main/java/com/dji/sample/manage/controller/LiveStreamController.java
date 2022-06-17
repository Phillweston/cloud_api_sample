package com.dji.sample.manage.controller;

import com.dji.sample.common.model.CustomClaim;
import com.dji.sample.common.model.ResponseResult;
import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.manage.model.Chan;
import com.dji.sample.manage.model.dto.CapacityDeviceDTO;
import com.dji.sample.manage.model.dto.LiveTypeDTO;
import com.dji.sample.manage.model.receiver.CapacityDeviceReceiver;
import com.dji.sample.manage.model.receiver.ServiceReplyReceiver;
import com.dji.sample.manage.service.ILiveStreamService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.dji.sample.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/19
 */

@RestController
@Slf4j
@RequestMapping("${url.manage.prefix}${url.manage.version}/live")
public class LiveStreamController {

    @Autowired
    private ILiveStreamService liveStreamService;

    /**
     * Analyze the live streaming capabilities of drones.
     * This data is necessary if drones are required for live streaming.
     * @param device    the capacity of drone
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_CAPACITY)
    public void stateCapacity(CapacityDeviceReceiver device) {
        boolean parseCapacity = liveStreamService.saveLiveCapacity(device);
        log.debug("The result of parsing the live capacity is {}.", parseCapacity);
    }

    /**
     * Get live capability data of all drones in the current user's workspace from the database.
     * @param request
     * @return  live capability
     */
    @GetMapping("/capacity")
    public ResponseResult<List<CapacityDeviceDTO>> getLiveCapacity(HttpServletRequest request) {
        // Get information about the current user.
        CustomClaim customClaim = (CustomClaim)request.getAttribute(TOKEN_CLAIM);

        List<CapacityDeviceDTO> liveCapacity = liveStreamService.getLiveCapacity(customClaim.getWorkspaceId());

        return ResponseResult.success(liveCapacity);
    }

    /**
     * Live streaming according to the parameters passed in from the web side.
     * @param liveParam Live streaming parameters.
     * @return
     */
    @PostMapping("/streams/start")
    public ResponseResult liveStart(@RequestBody LiveTypeDTO liveParam) {
        return liveStreamService.liveStart(liveParam);
    }

    /**
     * Stop live streaming according to the parameters passed in from the web side.
     * @param liveParam Live streaming parameters.
     * @return
     */
    @PostMapping("/streams/stop")
    public ResponseResult liveStop(@RequestBody LiveTypeDTO liveParam) {
        return liveStreamService.liveStop(liveParam.getVideoId());
    }

    /**
     * Set the quality of the live streaming according to the parameters passed in from the web side.
     * @param liveParam Live streaming parameters.
     * @return
     */
    @PostMapping("/streams/update")
    public ResponseResult liveSetQuality(@RequestBody LiveTypeDTO liveParam) {
        return liveStreamService.liveSetQuality(liveParam);
    }

    /**
     * Handle the reply message from the pilot side to the on-demand video.
     * @param message   reply message
     * @throws IOException
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_SERVICE_REPLY)
    public void serviceReply(Message<?> message) throws IOException {
        byte[] payload = (byte[])message.getPayload();
        ObjectMapper mapper = new ObjectMapper();
        CommonTopicReceiver<ServiceReplyReceiver> receiver = mapper.readValue(payload,
                new TypeReference<CommonTopicReceiver<ServiceReplyReceiver>>() {
        });
        Chan<CommonTopicReceiver> chan = Chan.getInstance();
        // Put the message to the chan object.
        chan.put(receiver);
    }
}