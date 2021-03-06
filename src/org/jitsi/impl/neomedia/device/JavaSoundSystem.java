/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jitsi.impl.neomedia.device;

import java.lang.reflect.*;

/**
 * Auto-detection of FMJ audio capture devices.
 *
 * @author Ken Larson
 */
public class JavaSoundSystem
    extends AudioSystem
{
    private static final String LOCATOR_PROTOCOL = LOCATOR_PROTOCOL_JAVASOUND;

    public JavaSoundSystem()
        throws Exception
    {
        super(LOCATOR_PROTOCOL);
    }

    @Override
    protected void doInitialize()
        throws Exception
    {
        // Done using reflection to avoid compile-time dependency on FMJ.
        Class<?> clazz
            = Class.forName(
                    "net.sf.fmj.media.cdp.javasound.CaptureDevicePlugger");
        Method addCaptureDevices = clazz.getMethod("addCaptureDevices");
        Object captureDevicePlugger = clazz.newInstance();

        addCaptureDevices.invoke(captureDevicePlugger);
    }

    @Override
    protected String getRendererClassName()
    {
        return "net.sf.fmj.media.renderer.audio.JavaSoundRenderer";
    }

    @Override
    public String toString()
    {
        return "JavaSound";
    }
}
