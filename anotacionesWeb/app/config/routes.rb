App::Application.routes.draw do
  get "users/index"

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


  match '/signin', :to => 'users#signin'
  match '/signout', :to => 'users#destroy'

  match '/user', :to => 'users#index'

  match '/sendeditednote', :to => 'users#updatenote'
end
