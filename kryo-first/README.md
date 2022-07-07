## Serializer
1. FieldSerializer
- field를 alphabetical 순서에 따라 저정한다.
- data만 저장한다. schema정보를 저장하지 않는다.
- field add, remove, change를 저장하지 않는다.
- field rename은 지원한다. 알파벳순서를 변경하지 않는다면
2. VersionFieldSerializer
- 필드추가만 지원 : @Since(int) 추가된 field에 annotation추가 
- 필드타입 변경, 필드삭제, 필드 이름변경을 지원하지 않는다.
3. TaggedFieldSerializer
- 필드타입 변경 지원하지 않음.
- 필드 추가, 삭제, 이름변경 지원
- @Tag(int) 있는 field만 저장됨. 
- 사용하지 않는 field는 @Deprecated로 
4. CompatibleFieldSerializer
 