/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jitsi.service.neomedia;

import java.beans.*;
import java.util.*;

import org.jitsi.service.neomedia.codec.*;
import org.jitsi.service.neomedia.device.*;
import org.jitsi.service.neomedia.format.*;

/**
 * The <tt>MediaService</tt> service is meant to be a wrapper of media libraries
 * such as JMF, FMJ, FFMPEG, and/or others. It takes care of all media play and
 * capture as well as media transport (e.g. over RTP).
 *
 * @author Emil Ivov
 * @author Lyubomir Marinov
 */
public interface MediaService
{
    /**
     * The name of the property of <tt>MediaService</tt> the value of which
     * corresponds to the value returned by
     * {@link #getDefaultDevice(MediaType, MediaUseCase)}. The <tt>oldValue</tt>
     * and the <tt>newValue</tt> of the fired <tt>PropertyChangeEvent</tt> are
     * not to be relied on and instead a call to <tt>getDefaultDevice</tt> is to
     * be performed to retrieve the new value.
     */
    public static final String DEFAULT_DEVICE = "defaultDevice";

    /**
     * Adds a <tt>PropertyChangeListener</tt> to be notified about changes in
     * the values of the properties of this instance.
     *
     * @param listener the <tt>PropertyChangeListener</tt> to be notified about
     * changes in the values of the properties of this instance
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Those interested in Recorder events add listener through MediaService.
     * This way they don't need to have access to the Recorder instance.
     * Adds a new <tt>Recorder.Listener</tt> to the list of listeners
     * interested in notifications from a <tt>Recorder</tt>.
     *
     * @param listener the new <tt>Recorder.Listener</tt> to be added to the
     * list of listeners interested in notifications from <tt>Recorder</tt>s.
     */
    public void addRecorderListener(Recorder.Listener listener);

    /**
     * Returns a new <tt>EncodingConfiguration</tt> instance.
     *
     * @return a new <tt>EncodingConfiguration</tt> instance.
     */
    public EncodingConfiguration createEmptyEncodingConfiguration();

    /**
     * Create a <tt>MediaStream</tt> which will use a specific
     * <tt>MediaDevice</tt> for capture and playback of media. The new instance
     * will not have a <tt>StreamConnector</tt> at the time of its construction
     * and a <tt>StreamConnector</tt> will be specified later on in order to
     * enable the new instance to send and receive media.
     *
     * @param device the <tt>MediaDevice</tt> to be used by the new instance for
     * capture and playback of media
     * @return a newly-created <tt>MediaStream</tt> which will use the specified
     * <tt>device</tt> for capture and playback of media
     */
    public MediaStream createMediaStream(MediaDevice device);

    /**
     * Initializes a new <tt>MediaStream</tt> of a specific <tt>MediaType</tt>.
     * The new instance will not have a <tt>MediaDevice</tt> at the time of its
     * initialization and a <tt>MediaDevice</tt> may be specified later on with
     * the constraint that {@link MediaDevice#getMediaType()} equals
     * <tt>mediaType</tt>.
     *
     * @param mediaType the <tt>MediaType</tt> of the new instance to be
     * initialized
     * @return a new <tt>MediaStream</tt> instance of the specified
     * <tt>mediaType</tt>
     */
    public MediaStream createMediaStream(MediaType mediaType);

    /**
     * Creates a <tt>MediaStream</tt> that will be using the specified
     * <tt>MediaDevice</tt> for both capture and playback of media exchanged
     * via the specified <tt>StreamConnector</tt>.
     *
     * @param connector the <tt>StreamConnector</tt> the stream should use for
     * sending and receiving media or <tt>null</tt> if the stream is to not have
     * a <tt>StreamConnector</tt> configured at initialization time and a
     * <tt>StreamConnector</tt> is to be specified later on
     * @param device the device to be used for both capture and playback of
     * media exchanged via the specified <tt>StreamConnector</tt>
     *
     * @return the newly created <tt>MediaStream</tt>.
     */
    public MediaStream createMediaStream(StreamConnector connector,
                                         MediaDevice     device);

    /**
     * Creates a <tt>MediaStream</tt> that will be using the specified
     * <tt>MediaDevice</tt> for both capture and playback of media exchanged
     * via the specified <tt>StreamConnector</tt>.
     *
     * @param connector the <tt>StreamConnector</tt> the stream should use for
     * sending and receiving media or <tt>null</tt> if the stream is to not have
     * a <tt>StreamConnector</tt> configured at initialization time and a
     * <tt>StreamConnector</tt> is to be specified later on
     * @param device the device to be used for both capture and playback of
     * media exchanged via the specified <tt>StreamConnector</tt>
     * @param zrtpControl a control which is already created, used to control
     * the ZRTP operations.
     *
     * @return the newly created <tt>MediaStream</tt>.
     */
    public MediaStream createMediaStream(StreamConnector connector,
                                         MediaDevice     device,
                                         SrtpControl zrtpControl);

    /**
     * Creates a new <tt>MediaDevice</tt> which uses a specific
     * <tt>MediaDevice</tt> to capture and play back media and performs mixing
     * of the captured media and the media played back by any other users of the
     * returned <tt>MediaDevice</tt>. For the <tt>AUDIO</tt> <tt>MediaType</tt>,
     * the returned device is commonly referred to as an audio mixer. The
     * <tt>MediaType</tt> of the returned <tt>MediaDevice</tt> is the same as
     * the <tt>MediaType</tt> of the specified <tt>device</tt>.
     *
     * @param device the <tt>MediaDevice</tt> which is to be used by the
     * returned <tt>MediaDevice</tt> to actually capture and play back media
     * @return a new <tt>MediaDevice</tt> instance which uses <tt>device</tt> to
     * capture and play back media and performs mixing of the captured media and
     * the media played back by any other users of the returned
     * <tt>MediaDevice</tt> instance
     */
    public MediaDevice createMixer(MediaDevice device);

    /**
     * Creates a new <tt>Recorder</tt> instance that can be used to record a
     * call which captures and plays back media using a specific
     * <tt>MediaDevice</tt>.
     *
     * @param device the <tt>MediaDevice</tt> which is used for media capture
     * and playback by the call to be recorded
     * @return a new <tt>Recorder</tt> instance that can be used to record a
     * call which captures and plays back media using the specified
     * <tt>MediaDevice</tt>
     */
    public Recorder createRecorder(MediaDevice device);

    /**
     * Initializes a new <tt>RTPTranslator</tt> which is to forward RTP and RTCP
     * traffic between multiple <tt>MediaStream</tt>s.
     *
     * @return a new <tt>RTPTranslator</tt> which is to forward RTP and RTCP
     * traffic between multiple <tt>MediaStream</tt>s
     */
    public RTPTranslator createRTPTranslator();

    /**
     * Initializes a new <tt>SrtpControl</tt> instance with a specific
     * <tt>SrtpControlType</tt>.
     *
     * @param srtpControlType the <tt>SrtpControlType</tt> of the new instance
     * @return a new <tt>SrtpControl</tt> instance with the specified
     * <tt>srtpControlType</tt>
     */
    public SrtpControl createSrtpControl(SrtpControlType srtpControlType);

    /**
     * Get available <tt>ScreenDevice</tt>s.
     *
     * @return screens
     */
    public List<ScreenDevice> getAvailableScreenDevices();

    /**
     * Returns the current <tt>EncodingConfiguration</tt> instance.
     *
     * @return the current <tt>EncodingConfiguration</tt> instance.
     */
    public EncodingConfiguration getCurrentEncodingConfiguration();

    /**
     * Returns the default <tt>MediaDevice</tt> for the specified media
     * <tt>type</tt>.
     *
     * @param mediaType a <tt>MediaType</tt> value indicating the kind of device
     * that we are trying to obtain.
     * @param useCase <tt>MediaUseCase</tt> value indicating for the use-case of
     * device that we are trying to obtain.
     *
     * @return the currently default <tt>MediaDevice</tt> for the specified
     * <tt>MediaType</tt>, or <tt>null</tt> if no such device exists.
     */
    public MediaDevice getDefaultDevice(
            MediaType mediaType,
            MediaUseCase useCase);

    /**
     * Get default <tt>ScreenDevice</tt> device.
     *
     * @return default screen device
     */
    public ScreenDevice getDefaultScreenDevice();

    /**
     * Returns a list containing all devices known to this service
     * implementation and handling the specified <tt>MediaType</tt>.
     *
     * @param mediaType the media type (i.e. AUDIO or VIDEO) that we'd like
     * to obtain the device list for.
     * @param useCase <tt>MediaUseCase</tt> value indicating for the use-case of
     * device that we are trying to obtain.
     *
     * @return the list of <tt>MediaDevice</tt>s currently known to handle the
     * specified <tt>mediaType</tt>.
     */
    public List<MediaDevice> getDevices(MediaType mediaType,
            MediaUseCase useCase);

    /**
     * Returns a {@link Map} that binds indicates whatever preferences the
     * media service implementation may have for the RTP payload type numbers
     * that get dynamically assigned to {@link MediaFormat}s with no static
     * payload type. The method is useful for formats such as "telephone-event"
     * for example that is statically assigned the 101 payload type by some
     * legacy systems. Signalling protocol implementations such as SIP and XMPP
     * should make sure that, whenever this is possible, they assign to formats
     * the dynamic payload type returned in this {@link Map}.
     *
     * @return a {@link Map} binding some formats to a preferred dynamic RTP
     * payload type number.
     */
    public Map<MediaFormat, Byte> getDynamicPayloadTypePreferences();

    /**
     * Gets the <tt>MediaFormatFactory</tt> through which <tt>MediaFormat</tt>
     * instances may be created for the purposes of working with the
     * <tt>MediaStream</tt>s created by this <tt>MediaService</tt>.
     *
     * @return the <tt>MediaFormatFactory</tt> through which
     * <tt>MediaFormat</tt> instances may be created for the purposes of working
     * with the <tt>MediaStream</tt>s created by this <tt>MediaService</tt>
     */
    public MediaFormatFactory getFormatFactory();

    /**
     * Gets the <tt>VolumeControl</tt> which controls the volume level of audio
     * input/capture.
     *
     * @return the <tt>VolumeControl</tt> which controls the volume level of
     * audio input/capture
     */
    public VolumeControl getInputVolumeControl();

    /**
     * Get a <tt>MediaDevice</tt> for a part of desktop streaming/sharing.
     *
     * @param width width of the part
     * @param height height of the part
     * @param x origin of the x coordinate (relative to the full desktop)
     * @param y origin of the y coordinate (relative to the full desktop)
     * @return <tt>MediaDevice</tt> representing the part of desktop or null
     * if problem
     */
    public MediaDevice getMediaDeviceForPartialDesktopStreaming(
            int width, int height, int x, int y);

    /**
     * Get origin for desktop streaming device.
     *
     * @param mediaDevice media device
     * @return origin
     */
    public java.awt.Point getOriginForDesktopStreamingDevice(
            MediaDevice mediaDevice);

    /**
     * Gets the <tt>VolumeControl</tt> which controls the volume level of audio
     * output/playback.
     *
     * @return the <tt>VolumeControl</tt> which controls the volume level of
     * audio output/playback
     */
    public VolumeControl getOutputVolumeControl();

    /**
     * Gives access to currently registered <tt>Recorder.Listener</tt>s.
     * @return currently registered <tt>Recorder.Listener</tt>s.
     */
    public Iterator<Recorder.Listener> getRecorderListeners();

    /**
     * Creates a preview component for the specified device(video device) used
     * to show video preview from it.
     *
     * @param device the video device
     * @param preferredWidth the width we prefer for the component
     * @param preferredHeight the height we prefer for the component
     * @return the preview component.
     */
    public Object getVideoPreviewComponent(
            MediaDevice device, int preferredWidth, int preferredHeight);

    /**
     * If the <tt>MediaDevice</tt> corresponds to partial desktop streaming
     * device.
     *
     * @param mediaDevice <tt>MediaDevice</tt>
     * @return true if <tt>MediaDevice</tt> is a partial desktop streaming
     * device, false otherwise
     */
    public boolean isPartialStreaming(MediaDevice mediaDevice);

    /**
     * Removes a <tt>PropertyChangeListener</tt> to no longer be notified about
     * changes in the values of the properties of this instance.
     *
     * @param listener the <tt>PropertyChangeListener</tt> to no longer be
     * notified about changes in the values of the properties of this instance
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes an existing <tt>Recorder.Listener</tt> from the list of listeners
     * interested in notifications from <tt>Recorder</tt>s.
     *
     * @param listener the existing <tt>Listener</tt> to be removed from the
     * list of listeners interested in notifications from <tt>Recorder</tt>s
     */
    public void removeRecorderListener(Recorder.Listener listener);
}
