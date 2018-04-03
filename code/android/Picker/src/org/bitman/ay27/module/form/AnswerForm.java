package org.bitman.ay27.module.form;

public class AnswerForm {
	private String raw;

	private long questionId;

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public AnswerForm() {
		
	}

	public AnswerForm(String raw, long questionId) {
		super();
		this.raw = raw;
		this.questionId = questionId;
	}

	public boolean checkFieldValidation() {
		if ((this.questionId > 0) && (this.raw != null)
				&& (!this.raw.equals(""))) {
			return true;
		}
		return false;
	}

}
