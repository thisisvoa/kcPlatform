package com.casic27.platform.base.file.addon;

import com.casic27.platform.base.file.context.FileUploadContext;

public class FileServiceAddonAdpater implements FileServiceAddon {

	@Override
	public boolean onBeforeUpload(FileUploadContext fileUploadContext) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onAfterUpload(FileUploadContext fileUploadContext) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onBeforeDelete(FileUploadContext fileUploadContext) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onAfterDelete(FileUploadContext fileUploadContext) {
		// TODO Auto-generated method stub

	}

}
