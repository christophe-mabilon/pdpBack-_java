package com.b3al.spring.jwt.mongodb.models;

import jakarta.servlet.http.Part;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BASE64DecodedMultipartFile implements MultipartFile {

    private final byte[] fileContent;

    public BASE64DecodedMultipartFile ( byte[] fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream (fileContent);
    }

    /**
     * Return a Resource representation of this MultipartFile. This can be used
     * as input to the {@code RestTemplate} or the {@code WebClient} to expose
     * content length and the filename along with the InputStream.
     *
     * @return this MultipartFile adapted to the Resource contract
     * @since 5.1
     */
    @Override
    public Resource getResource () {
        return MultipartFile.super.getResource ( );
    }

    /**
     * Transfer the received file to the given destination file.
     * <p>This may either move the file in the filesystem, copy the file in the
     * filesystem, or save memory-held contents to the destination file. If the
     * destination file already exists, it will be deleted first.
     * <p>If the target file has been moved in the filesystem, this operation
     * cannot be invoked again afterwards. Therefore, call this method just once
     * in order to work with any storage mechanism.
     * <p><b>NOTE:</b> Depending on the underlying provider, temporary storage
     * may be container-dependent, including the base directory for relative
     * destinations specified here (e.g. with Servlet multipart handling).
     * For absolute destinations, the target file may get renamed/moved from its
     * temporary location or newly copied, even if a temporary copy already exists.
     *
     * @param dest the destination file (typically absolute)
     * @throws IOException           in case of reading or writing errors
     * @throws IllegalStateException if the file has already been moved
     *                               in the filesystem and is not available anymore for another transfer
     * @see Part#write(String)
     */
    @Override
    public void transferTo ( File dest ) throws IOException, IllegalStateException {

    }

    @Override
    public void transferTo(java.nio.file.Path dest) throws IOException, IllegalStateException {
        // Implement this method if necessary
    }
}