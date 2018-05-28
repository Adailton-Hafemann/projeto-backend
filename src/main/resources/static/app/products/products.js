'use strict';

angular.module('myApp.products', ['ngRoute'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/products', {
      templateUrl: 'app/products/products.html',
      controller: 'ProductsController'
    });
  }])
  .controller('ProductsController', function($scope, $http, $mdDialog) {
    var tree;

    $scope.data = [];
    $scope.finalData = [];

    $scope.treeControl = tree = {};

    $http.get('products').then(function(response) {
      $scope.data = angular.copy(response.data.products);
      $scope.finalData = angular.copy(response.data.products);
    });

    $scope.addProduct = function(ev) {
      $mdDialog.show({
        controller: DialogController,
        templateUrl: 'app/products/product.html',
        parent: angular.element(document.body),
        targetEvent: ev,
        clickOutsideToClose:true,
        locals : {
          parentProduct : tree.get_selected_branch(),
          product : {}
        }
      }).then(onSaveProduct);
    };

    $scope.filterProducts = function() {
      var data = angular.copy($scope.finalData);

      if ($scope.searchFilter != '')
        angular.forEach(data , function(node ,  index){
          filter(node , $scope.searchFilter);
        });

      $scope.data = data;
    };

    function filter(node, query){
      if(node.children && node.children.length != 0) {
        angular.forEach(node.children , function(childNode ,  index){
          filter(childNode , query);
        });
      }
      node.expanded = true;

      if( node.label.indexOf(query) == -1 && (!node.children || hasOnlyInvisibleChildren(node)) )
        node.visible = false;
    }

    $scope.updateProduct = function(ev) {
      var selectedProduct = tree.get_selected_branch();
      $mdDialog.show({
        controller: DialogController,
        templateUrl: 'app/products/product.html',
        parent: angular.element(document.body),
        targetEvent: ev,
        clickOutsideToClose:true,
        locals : {
          parentProduct : tree.get_parent_branch(tree.get_selected_branch()),
          product : {
            id : selectedProduct.id,
            label : selectedProduct.label,
            code : selectedProduct.code,
            observation : selectedProduct.observation
          }
        }
      }).then(onUpdateProduct);
    };

    $scope.removeProduct = function(ev) {
      var product = tree.get_selected_branch();
      if (product)
        $http.delete('products/' + product.id, {}).then(onDeleteProduct)
    };

    function hasOnlyInvisibleChildren(node) {
      for (var i = 0; i < node.children.length; i++)
        if (node.children[i].visible === true)
          return false;
      return true;
    }

    function onSaveProduct(answer) {
      if (answer.status === 200) {
        tree.add_branch(tree.get_selected_branch(), answer.data);
        loadFinalData();
      }
    }

    function onUpdateProduct(answer) {
      if (answer.status === 200) {
        var selectedProduct = tree.get_selected_branch();
        selectedProduct.label = answer.data.label;
        selectedProduct.code = answer.data.code;
        selectedProduct.observation = answer.data.observation;
        selectedProduct.tooltip = answer.data.tooltip;
        loadFinalData();
      }
    }

    function onDeleteProduct() {
      deleteNode(tree.get_selected_branch());
      tree.select_branch(null);
      loadFinalData();
    }

    function deleteNode(node) {
      if (!node.parent_uid)
        $scope.data = filterArray($scope.data, node.id);
      else {
        var parent = tree.get_parent_branch(node);
        parent.children = filterArray(parent.children, node.id);
      }
    }

    function loadFinalData() {
      $http.get('products').then(function(response) {
        $scope.finalData = response.data.products;
      });
    }

    function filterArray(data, value) {
      return data.filter(function(item) {
        return item.id !== value;
      });
    }

    function DialogController($scope, $mdDialog, parentProduct, product) {
      $scope.product = product;
      $scope.parentProduct = parentProduct;

      $scope.hide = function() {
        $mdDialog.hide();
      };

      $scope.cancel = function() {
        $mdDialog.cancel();
      };

      $scope.answer = function(answer) {
        $mdDialog.hide($http.post('products' + ($scope.parentProduct ? '/' + $scope.parentProduct.id : ''), $scope.product));
      };
    }

  });
