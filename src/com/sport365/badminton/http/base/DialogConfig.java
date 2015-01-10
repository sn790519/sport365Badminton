package com.sport365.badminton.http.base;

import com.sport365.badminton.R;

/**
 * 请求信息，
 * 
 */
public final class DialogConfig {

	private volatile int loadingMessage; // Lazily initialized.
	private volatile boolean cancelable; // Lazily initialized.

	private DialogConfig(Builder builder) {
		this.loadingMessage = builder.loadingMessage;
		this.cancelable = builder.cancelable;

	}

	public int loadingMessage() {
		return loadingMessage;
	}

	public boolean cancelable() {
		return cancelable;
	}

	public Builder newBuilder() {
		return new Builder(this);
	}

	@Override
	public String toString() {
		return "RequestInfo{loadingMessage=" + loadingMessage + ", cancelable=" + cancelable + '}';
	}

	public static class Builder {
		private int loadingMessage;
		private boolean cancelable;

		public Builder() {
			this.loadingMessage = R.string.loading_public_default;
			this.cancelable = true;
		}

		private Builder(DialogConfig request) {
			this.loadingMessage = request.loadingMessage;
			this.cancelable = request.cancelable;
		}

		public Builder loadingMessage(int loadingMessage) {
			if (loadingMessage == 0)
				throw new IllegalArgumentException("loadingMessage must have value");
			this.loadingMessage = loadingMessage;
			return this;
		}

		public Builder cancelable(boolean cancelable) {
			this.cancelable = cancelable;
			return this;
		}

		public DialogConfig build() {
			return new DialogConfig(this);
		}
	}
}
