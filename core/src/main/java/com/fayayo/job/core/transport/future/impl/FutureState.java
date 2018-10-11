
package com.fayayo.job.core.transport.future.impl;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc 异步任务的执行状态
 */
public enum FutureState {
	/** the task is doing **/
	DOING(0),
	/** the task is done **/
	DONE(1),
	/** ths task is cancelled **/
	CANCELLED(2);

	public final int value;

	private FutureState(int value) {
		this.value = value;
	}

	public boolean isCancelledState() {
		return this == CANCELLED;
	}

	public boolean isDoneState() {
		return this == DONE;
	}

	public boolean isDoingState() {
		return this == DOING;
	}
}
