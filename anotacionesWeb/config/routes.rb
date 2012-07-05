AnotacionesWeb::Application.routes.draw do
  get "pages/index"
  get "pages/nota"
  get "pages/questions"

  root :to => 'pages#index'

end
