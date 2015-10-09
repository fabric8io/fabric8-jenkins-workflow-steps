/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jenkinsci.plugins.fabric8.support.hack;

import com.jcraft.jzlib.GZIPInputStream;
import com.jcraft.jzlib.GZIPOutputStream;
import com.trilead.ssh2.crypto.Base64;
import hudson.console.ConsoleAnnotationOutputStream;
import hudson.console.ConsoleAnnotator;
import hudson.console.PlainTextConsoleOutputStream;
import hudson.remoting.ObjectInputStreamEx;
import hudson.util.TimeUnit2;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import jenkins.model.Jenkins;
import jenkins.security.CryptoConfidentialKey;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.framework.io.ByteBuffer;

public class AnnotatedLargeText<T> extends LargeText {
    private T context;
    private static final CryptoConfidentialKey PASSING_ANNOTATOR = new CryptoConfidentialKey(AnnotatedLargeText.class, "consoleAnnotator");

    public AnnotatedLargeText(File file, Charset charset, boolean completed, T context) {
        super(file, charset, completed, true);
        this.context = context;
    }

    public AnnotatedLargeText(ByteBuffer memory, Charset charset, boolean completed, T context) {
        super(memory, charset, completed);
        this.context = context;
    }

    public void doProgressiveHtml(StaplerRequest req, StaplerResponse rsp) throws IOException {
        req.setAttribute("html", Boolean.valueOf(true));
        this.doProgressText(req, rsp);
    }

    public void doProgressiveText(StaplerRequest req, StaplerResponse rsp) throws IOException {
        this.doProgressText(req, rsp);
    }

    private boolean isHtml() {
        StaplerRequest req = Stapler.getCurrentRequest();
        return req != null && req.getAttribute("html") != null;
    }

    protected void setContentType(StaplerResponse rsp) {
        rsp.setContentType(this.isHtml()?"text/html;charset=UTF-8":"text/plain;charset=UTF-8");
    }

    private ConsoleAnnotator createAnnotator(StaplerRequest req) throws IOException {
        try {
            String e = req != null?req.getHeader("X-ConsoleAnnotator"):null;
            if(e != null) {
                Cipher sym = PASSING_ANNOTATOR.decrypt();
                ObjectInputStreamEx ois = new ObjectInputStreamEx(new GZIPInputStream(new CipherInputStream(new ByteArrayInputStream(Base64.decode(e.toCharArray())), sym)), Jenkins.getInstance().pluginManager.uberClassLoader);

                ConsoleAnnotator var7;
                try {
                    long timestamp = ois.readLong();
                    if(TimeUnit2.HOURS.toMillis(1L) <= Math.abs(System.currentTimeMillis() - timestamp)) {
                        return ConsoleAnnotator.initial(this.context == null?null:this.context.getClass());
                    }

                    var7 = (ConsoleAnnotator)ois.readObject();
                } finally {
                    ois.close();
                }

                return var7;
            }
        } catch (ClassNotFoundException var12) {
            throw new IOException(var12);
        }

        return ConsoleAnnotator.initial(this.context == null?null:this.context.getClass());
    }

    public long writeLogTo(long start, Writer w) throws IOException {
        return this.isHtml()?this.writeHtmlTo(start, w):super.writeLogTo(start, w);
    }

    public long writeLogTo(long start, OutputStream out) throws IOException {
        return super.writeLogTo(start, new PlainTextConsoleOutputStream(out));
    }

    public long writeRawLogTo(long start, OutputStream out) throws IOException {
        return super.writeLogTo(start, out);
    }

    public long writeHtmlTo(long start, Writer w) throws IOException {
        ConsoleAnnotationOutputStream caw = new ConsoleAnnotationOutputStream(w, this.createAnnotator(Stapler.getCurrentRequest()), this.context, this.charset);
        long r = super.writeLogTo(start, caw);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Cipher sym = PASSING_ANNOTATOR.encrypt();
        ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(new CipherOutputStream(baos, sym)));
        oos.writeLong(System.currentTimeMillis());
        oos.writeObject(caw.getConsoleAnnotator());
        oos.close();
        StaplerResponse rsp = Stapler.getCurrentResponse();
        if(rsp != null) {
            rsp.setHeader("X-ConsoleAnnotator", new String(Base64.encode(baos.toByteArray())));
        }
        return r;
    }

    @Override
    public long writeLogTo(long start, int size, Writer w) throws IOException {
        if (isHtml()) {
            ConsoleAnnotationOutputStream caw = new ConsoleAnnotationOutputStream(w, this.createAnnotator(Stapler.getCurrentRequest()), this.context, this.charset);
            long r = super.writeLogTo(start, size, caw);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Cipher sym = PASSING_ANNOTATOR.encrypt();
            ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(new CipherOutputStream(baos, sym)));
            oos.writeLong(System.currentTimeMillis());
            oos.writeObject(caw.getConsoleAnnotator());
            oos.close();
            StaplerResponse rsp = Stapler.getCurrentResponse();
            if(rsp != null) {
                rsp.setHeader("X-ConsoleAnnotator", new String(Base64.encode(baos.toByteArray())));
            }
            return r;
        } else {
            return super.writeLogTo(start, size, w);
        }
    }
}
