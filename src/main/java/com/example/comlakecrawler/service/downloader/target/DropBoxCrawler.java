package com.example.comlakecrawler.service.downloader.target;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.GetMetadataErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DropBoxCrawler {
    private DbxClientV2 client;
    private static final String ACCESS_TOKEN = "TyPf_XQoz_AAAAAAAAAAAZ4qLLuKENU9BGjUjkMKDlidV4A7tgb0sREa5qBY48vx";

    public DropBoxCrawler() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/crawler_csv").build();
        client = new DbxClientV2(config,ACCESS_TOKEN);
        try {
            FullAccount account = client.users().getCurrentAccount();
            System.out.println(account.getName().getDisplayName());
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

    public void searchAndDownloadMachine(String topic){
        ListFolderResult result = null;
        try {
            result = client.files().listFolder("");
            while (true) {
                for (Metadata metadata : result.getEntries()) {
//                System.out.println(metadata.getPathLower());
                    if (metadata.getPathLower().contains(topic)){
//                    download(client,metadata.getPathLower().replace("/",""),metadata);
                        System.out.println(metadata.getPathLower());
                    }
                }
                if (!result.getHasMore()) {
                    break;
                }

                result = client.files().listFolderContinue(result.getCursor());
            }
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }
    public void download(String filename, Metadata pathMetadata){
        String path = "D:\\save\\sources";
        File file = new File(path, filename);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            client.files().download(pathMetadata.getPathLower()).download(outputStream);
            System.out.println("METADATA"+  pathMetadata.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DownloadErrorException e) {
            e.printStackTrace();
        } catch (GetMetadataErrorException e) {
            e.printStackTrace();
        } catch (DbxException e) {
            e.printStackTrace();
        }

    }
}
