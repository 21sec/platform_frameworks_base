/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package android.net.wifi.hotspot2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.net.wifi.EAPConstants;
import android.net.wifi.hotspot2.pps.Credential;
import android.net.wifi.hotspot2.pps.HomeSP;
import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

/**
 * Unit tests for {@link android.net.wifi.hotspot2.PasspointConfiguration}.
 */
@SmallTest
public class PasspointConfigurationTest {

    private static HomeSP createHomeSp() {
        HomeSP homeSp = new HomeSP();
        homeSp.fqdn = "fqdn";
        homeSp.friendlyName = "friendly name";
        homeSp.roamingConsortiumOIs = new long[] {0x55, 0x66};
        return homeSp;
    }

    private static Credential createCredential() {
        Credential cred = new Credential();
        cred.realm = "realm";
        cred.userCredential = null;
        cred.certCredential = null;
        cred.simCredential = new Credential.SimCredential();
        cred.simCredential.imsi = "1234*";
        cred.simCredential.eapType = EAPConstants.EAP_SIM;
        cred.caCertificate = null;
        cred.clientCertificateChain = null;
        cred.clientPrivateKey = null;
        return cred;
    }

    private static void verifyParcel(PasspointConfiguration writeConfig) throws Exception {
        Parcel parcel = Parcel.obtain();
        writeConfig.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);    // Rewind data position back to the beginning for read.
        PasspointConfiguration readConfig =
                PasspointConfiguration.CREATOR.createFromParcel(parcel);
        assertTrue(readConfig.equals(writeConfig));
    }

    /**
     * Verify parcel read/write for a default configuration.
     *
     * @throws Exception
     */
    @Test
    public void verifyParcelWithDefault() throws Exception {
        verifyParcel(new PasspointConfiguration());
    }

    /**
     * Verify parcel read/write for a configuration that contained both HomeSP and Credential.
     * @throws Exception
     */
    @Test
    public void verifyParcelWithHomeSPAndCredential() throws Exception {
        PasspointConfiguration config = new PasspointConfiguration();
        config.homeSp = createHomeSp();
        config.credential = createCredential();
        verifyParcel(config);
    }

    /**
     * Verify parcel read/write for a configuration that contained only HomeSP.
     *
     * @throws Exception
     */
    @Test
    public void verifyParcelWithHomeSPOnly() throws Exception {
        PasspointConfiguration config = new PasspointConfiguration();
        config.homeSp = createHomeSp();
        verifyParcel(config);
    }

    /**
     * Verify parcel read/write for a configuration that contained only Credential.
     *
     * @throws Exception
     */
    @Test
    public void verifyParcelWithCredentialOnly() throws Exception {
        PasspointConfiguration config = new PasspointConfiguration();
        config.credential = createCredential();
        verifyParcel(config);
    }

    /**
     * Verify that a default/empty configuration is invalid.
     *
     * @throws Exception
     */
    @Test
    public void validateDefaultConfig() throws Exception {
        PasspointConfiguration config = new PasspointConfiguration();
        assertFalse(config.validate());
    }

    /**
     * Verify that a configuration without Credential is invalid.
     *
     * @throws Exception
     */
    @Test
    public void validateConfigWithoutCredential() throws Exception {
        PasspointConfiguration config = new PasspointConfiguration();
        config.homeSp = createHomeSp();
        assertFalse(config.validate());
    }

    /**
     * Verify that a a configuration without HomeSP is invalid.
     *
     * @throws Exception
     */
    @Test
    public void validateConfigWithoutHomeSp() throws Exception {
        PasspointConfiguration config = new PasspointConfiguration();
        config.credential = createCredential();
        assertFalse(config.validate());
    }

    /**
     * Verify a valid configuration.
     *
     * @throws Exception
     */
    @Test
    public void validateValidConfig() throws Exception {
        PasspointConfiguration config = new PasspointConfiguration();
        config.homeSp = createHomeSp();
        config.credential = createCredential();
        assertTrue(config.validate());
    }
}