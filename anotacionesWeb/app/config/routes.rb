App::Application.routes.draw do
  get "pages/index"
  get "pages/login"
  get "pages/error"
  get "pages/note"
  get "pages/hola"
  get "pages/update_note"

  post "pages/login"
  post "pages/note"
  post "pages/hola"
  post "pages/update_note"

  root :to => 'pages#index'
end
