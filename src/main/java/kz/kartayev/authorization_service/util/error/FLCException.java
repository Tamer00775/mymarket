package kz.kartayev.authorization_service.util.error;

import lombok.Getter;

@Getter
public class FLCException extends IllegalArgumentException{
  private String messageRu;
  private String messageKk;
  private String code;
  public FLCException(String code, String messageRu, String messageKk) {
    super(messageRu);
    this.messageKk = messageKk;
    this.code = code;
  }
  public FLCException(String messageRu) {
    super(messageRu);
  }

}
