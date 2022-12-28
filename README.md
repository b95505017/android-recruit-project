大致上都遵照 Android 官方的架構設計，除了最後的 UI 是 `Jetpack Compose`：
<img src="https://developer.android.com/static/topic/libraries/architecture/images/paging3-layered-architecture.svg">
 
抽象層是 `HahowClassRepository`，利用 Paging + Room 的實作層是 `DefaultClassRepository`，並利用 `Hilt` 綁定抽象層跟實作層的關聯

Demo (含深色模式):

https://user-images.githubusercontent.com/320815/209850108-cca4ff1a-6c28-4136-a568-a13a92337d5e.mp4
